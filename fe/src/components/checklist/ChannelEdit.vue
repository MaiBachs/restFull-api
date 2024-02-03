<script setup>
import {defineProps, computed, ref} from "vue"
import {defineEmits} from "vue"
import axios from 'axios';
import { notify } from "@kyvg/vue3-notification";
import Multiselect from "@vueform/multiselect"
import{getChannelTypeChecklist} from "@/store/getChannelTypeChecklist";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from './../../store/LoaddingStore'
import { i18n } from '../../main.js';
import { getChannelType} from "@/store/getChannelType";
const getlistPlan = getChannelType()
const loaddingStore = useLoaddingStore()
const listChannelTypeChecklist = getChannelTypeChecklist();
const emit = defineEmits(['onOk', 'update:modelValue'])
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  optionsobjectlevel: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  options: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  channel_type_name: {
    type: Object,
    required: true,
    default: () => ({}),
  }

});
const optionsStatus = [
  {
    title:i18n.global.t('ACTIVE'),
    value: 1,
  },
  {
    title: i18n.global.t('INACTIVE'),
    value: 0,
  }
];

const itemDetail = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value)
  }
},);

const isSaving = ref(false);
async function confirmEditChannel (action){
  console.log(itemDetail)
  loaddingStore.loading = true;
  try {
    isSaving.value = true;
    const response = await axios.post('/api/plan/edit', {
      channel_type_id: itemDetail.value.channel_type_id,
      channel_type_name: itemDetail.value.channel_type_name,
      plan_name: itemDetail.value.plan_name,
      object_type: itemDetail.value.object_type,
      object_level: itemDetail.value.object_level,
      plan_id: itemDetail.value.plan_id,
      status: itemDetail.value.status,
    });
    isSaving.value = false;
    console.log(response.data.data)
    if (response && response.data.status && response.data.status.message) {
      notify({
        type: 'error',
        title: "Edit Error",
        text: response.data.status.message,
        duration: 10000,
      });
      loaddingStore.loading = false;
    } else {
      notify({
        type: 'success',
        title: "Edit Successful",
        duration: 10000,
      });
      getlistPlan.actionGetListFillPlan()
      action.onClose();
      emit('clearValue') //clear thong tin va search lai
    }
  } catch (error) {

    console.error(error);
    isSaving.value = false;
  }
}

// const selectedObjectType = ref(props.initialObjectType);

// watch(() => props.initialObjectType, (newInitialObjectType) => {
//   selectedObjectType.value = newInitialObjectType;
// });
// const selectedObjectLevel = ref(props.initialObjectLevel);

// watch(() => props.initialObjectLevel, (newInitialObjectLevel) => {
//   selectedObjectLevel.value = newInitialObjectLevel;
// });

const showObjectLevel = ref(false);
const handleChannelTypeChange = async (selectedItemId) => {
  console.log(selectedItemId)
  showObjectLevel.value = false;
  if (itemDetail.value.channel_type_id === 80043 || itemDetail.value.channel_type_id === 10 || itemDetail.value.channel_type_id === 80043 || itemDetail.value.channel_type_id === 1000321 || itemDetail.value.channel_type_id === 11) {
    showObjectLevel.value = true;
    itemDetail.value.object_type = 2;
  } else {
    itemDetail.value.object_type = 1;
    showObjectLevel.value = false;
  }

  const selectedItem = listChannelTypeChecklist.listChannelTypeChecklist.find(item => item.value === selectedItemId);
    if (selectedItem) {
      itemDetail.value.object_type = selectedItem.object_type
    }
};
const clearChannelType = () => {
  itemDetail.value.channel_type_id = null; 
  itemDetail.value.object_level = null;
  itemDetail.value.object_type = null;
  showObjectLevel.value = false;
};

const isConfirmEnabled = computed(() => {
  return (
    itemDetail.value.status !== null &&
    itemDetail.value.channel_type_id !== null &&
    itemDetail.value.plan_name !== ""
  );
});



</script>
<template>
  <popupModal modal-id="exampleModal" :title="$t('EDIT_PLANN')">
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("PLAN_NAME") }}:</label>
          <input
              v-model="itemDetail.plan_name"
              class="form-control-input col-md-7"
              type="text"
              style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end;margin: 0 auto;min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;padding-left: 26px;font-size: medium;font-weight: 500;"
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:</label>
          <Multiselect
              v-model="itemDetail.channel_type_id"
              :options="listChannelTypeChecklist.listChannelTypeChecklist || []"
              :placeholder="$t('SELECT_ONE')"
              deselect-label="Can't remove this value"
              track-by="label"
              label="label"
              :searchable="false"
              :allow-empty="false"
              style="width: 300px;" 
              @select="handleChannelTypeChange(itemDetail.channel_type_id)"
              @clear="clearChannelType"
          ></Multiselect>
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center" v-if="itemDetail.channel_type_id === 80043">
        <div class="row">
          <label class="col-md-5 form-label"
          >{{ $t("OBJECT_LEVEL") }}:</label>
          <Multiselect
              v-model="itemDetail.object_level"
              deselect-label="Can't remove this value"
              track-by="title"
              label="title"
              :placeholder="$t('SELECT_ONE')"
              :options="props.optionsobjectlevel"
              :searchable="false"
              :allow-empty="false"
              style="width: 300px;"
              
              >
          </Multiselect>
        </div>
      </div>
      
     <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
          >{{ $t("STATUS") }}:</label>
          <Multiselect
              v-model="itemDetail.status"
              deselect-label="Can't remove this value"
              track-by="title"
              label="title"
              :placeholder="$t('SELECT_ONE')"
              :options="optionsStatus"
              :searchable="false"
              :allow-empty="false"
              style="width: 300px;">
          </Multiselect>
        </div>
      </div>
      <div class="col-md-6 text-center"></div>
    </div>
    <div class="row col-md-12 p-b-15">
    </div>
    <template #modalActions="modalActionProps">
      <button  type="submit" class="btn btn-primary ng-binding" style="width: 175px; text-align: center;" @click="confirmEditChannel(modalActionProps)" :disabled="!isConfirmEnabled">
        {{ $t("CONFIRM") }}
      </button>
    </template>
  </popupModal>


</template>
<style scoped>
.form-label {
  text-align: start;
}
.form-control-input {
  height: 30px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
}
.form-control-date {
  width: 58%;
  padding: 0px;
  height: 35px;
}
</style>
