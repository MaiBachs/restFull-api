<script setup>
import { reactive, ref, defineProps, defineEmits } from "vue";
import { i18n } from "@/main.js";
import Multiselect from "@vueform/multiselect";
import popupModal from "@/components/common/popupModalCommon.vue";
import useChannelManager from "@/composables/useChannelManager"
import { useLoaddingStore } from "@/store/LoaddingStore";
import ErrorModal from '@/components/common/errorModal.vue'
import { useUserStore } from '@/store/UserStore';

import { notify } from "@kyvg/vue3-notification";
const popupImportResults = ref(false)
const questionImport = ref(false)
const loaddingStore = useLoaddingStore();
const errorMessageFlag = ref(false);
const quantitySuccess = ref("");
const fileNameError = ref("");
const emit = defineEmits(['update:modelValue'])
const userStore = useUserStore()
const props = defineProps({
    searchData: {
    type: Object,
    required: true,
    default: () => ({}),
  },bcCode: {
    type: Object,
    required: true,
    default: () => ({}),
  },brCode: {
    type: Object,
    required: true,
    default: () => ({}),
  },

  
});

const searchDataCommon = reactive({
  br_code: "",
  bc_code: "",
  channel_id: "",
  plan_date: "",
  target_level: ""
});

console.log(props.brCode)
const  {importChannelManager, importResults, downloadFileError} = useChannelManager(ref);
function updateSelectedCodeBC(selectedValue) {
  console.log(selectedValue)
  const selectedBC = userStore.listBC.find(bc => bc.value === selectedValue);
    if (selectedBC.value != 7282) {
      searchDataCommon.bc_code = selectedBC.code;

    }else {
      searchDataCommon.bc_code = ""
    }
  }

function updateSelectedCode(selectedValue) {
  
  console.log(selectedValue)
  const selectedBranch = userStore.listBranch.find(br => br.value === selectedValue);
    if (selectedBranch.value != 7282) {
      searchDataCommon.br_code = selectedBranch.code;
    }else {
      searchDataCommon.br_code = ""
    }
    console.log(searchDataCommon.br_code)
}
const listTypeTemplate = reactive([
  { value: 1, name: i18n.global.t("DEVELOP_NEW") },
  { value: 2, name: i18n.global.t("CUMULATIVE_CHANNEL") },
]);
const typeImport = ref("");
const inputFile = ref("");
const fileData = ref("");

typeImport.value = listTypeTemplate[0].value
const previewFiles = async () => {
  fileData.value = inputFile.value.files[0];
};

const importChannel = async (action) => {
  updateSelectedCodeBC(props.bcCode)
  updateSelectedCode(props.brCode)
  searchDataCommon.channel_id = props.searchData.channel_id
  searchDataCommon.plan_date = props.searchData.plan_date
  searchDataCommon.target_level = props.searchData.target_level
  await importChannelManager(searchDataCommon,fileData,typeImport)
  if( importResults.value && importResults.value.data.status && importResults.value.data.status.message ) {
      popupImportResults.value = true
      errorMessageFlag.value = true
      quantitySuccess.value = importResults.value.data.status.code
      fileNameError.value = importResults.value.data.status.message
    } else {
      questionImport.value = false
      inputFile.value.value = null;
        action.onClose();
        emit('clearValue')
      notify({
        type: "success",
        title: i18n.global.t("VISIT_PLAN.IMPORT.LABEL.IMPORT"),
        text: importResults.value.data.status.code,
        duration: 3000,
      });
      loaddingStore.loading = false;
    }
  
}
const downloadErrorImmportFile = () => {
  downloadFileError(fileNameError.value)
};

</script>

<template>
  <popupModal modal-id="exampleModalSelectTypeImport" :title="$t('CHANNEL_MANAGEMENT')" @close-modal="closeModal">
    <div class="col-xs-12">
      <div class="m-b-25 row">
        <div class="col-sm-3">
          <label class="col-form-label">{{ $t("TEMPALTE_TYPE") }}:</label>
        </div>
        <div class="col-sm-7">
          <Multiselect
            v-model="typeImport"
            :options="listTypeTemplate"
            track-by="name"
            label="name"
            :canClear="false"
          ></Multiselect>
        </div>
      </div>
    </div>

    <div v-if="typeImport" class="col-xs-12">
      <div class="m-b-25 row">
        <div class="col-sm-3">
          <label class="col-form-label"
            >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
          >
        </div>
        <div class="col-sm-7">
          <form>
            <input
              id="file"
              ref="inputFile"
              accept=".xls, .xlsx"
              type="file"
              @change="previewFiles"
            />
          </form>
        </div>
      </div>
    </div>
    <template #modalActions="modalActionProps">
        <button v-if="fileData" class="btn btn-primary" type="button" @click="importChannel(modalActionProps)">
          {{ $t("SUBMIT") }}
        </button>
    </template>
  </popupModal>

  <ErrorModal v-model="popupImportResults" :title="$t('TARGET_RESULTS')">
    <div class="text-center p-10">
      <label>{{quantitySuccess}}</label>
    </div>
    <template #modalActionsError="modalActionsErrorProps">
        <button class="btn btn-danger" type="button" @click="downloadErrorImmportFile(modalActionsErrorProps)">
          {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
        </button>
    </template>
  </ErrorModal>
</template>
