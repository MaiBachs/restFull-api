<script setup>
import {defineProps, computed, ref} from "vue"
import {defineEmits} from "vue"
import axios from 'axios';
import { notify } from "@kyvg/vue3-notification";
import popupModal from '../../app/popupModal.vue'
// import {getParent} from "@/store/getParent";
// import Multiselect from "@vueform/multiselect"
// const listParent2 = ref({})
// const listParent = getParent();
import { useLoaddingStore } from './../../store/LoaddingStore'

const loaddingStore = useLoaddingStore()
const emit = defineEmits(['onOk', 'update:itemDetail'])
const props = defineProps({
  itemDetail: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  actionGetParent2: {
    type: Object,
    required: true,
    default: () => ({}),
  },

});
const itemDetail = computed({
  get() {
    return props.itemDetail || {name: '', code: '', job_id: '',job_id_lv2:''};
  },
  set(value) {
    emit('update:itemDetail', value)
  }
});
// const actionGetParent2 = async (item) =>{
//   console.log(item)
//   listParent2.value = []
//   const Parent2 = await axios.post('/api/job/fill-check-list-by-parentId',{
//         parent_id: item
//       });
//   if(Parent2.data && Parent2.data.length) {
//     listParent2.value  = Parent2.data.map(function (o) {
//       return {
//         value: o.job_id,
//         label: o.code,
//       };
//     })
//   }
//   console.log(listParent2);

// }
const isSaving = ref(false);
async function confirmEditChecklist (action) {
  loaddingStore.loading = true;
  try {
    isSaving.value = true;
    const response = await axios.put('/api/job/edit-checklist', {
      code: itemDetail.value.code,
      name: itemDetail.value.name,
      parent_name: itemDetail.value.parent_name,
      job_id: itemDetail.value.job_id,
      parent_id: itemDetail.value.parent_id,
    });
    isSaving.value = false;
    console.log(response.data);
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
      loaddingStore.loading = false;
      action.onClose();
      emit('clearValue') //clear thong tin va search lai
    }
  } catch (error) {
    console.error(error);
    isSaving.value = false;
  }
}




</script>
<template>
  <popupModal modal-id="exampleModalEditChecklist" :title="$t('EDIT_CHECKLIST')">
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.NAME") }}:</label>
          <input
              v-model="itemDetail.name"
              class="form-control-input col-md-7"
              type="text"
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.CODE") }}:</label>
          <input
              v-model="itemDetail.code"
              class="form-control-input col-md-7"
              type="text"
          />
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <!-- <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >Level 1:</label>
            <Multiselect
                v-model="itemDetail.job_id"
                :options="listParent.listParent || []"
                :placeholder="$t('SELECT_ONE')"
                deselect-label="Can't remove this value"
                track-by="label"
                label="label"
                :searchable="false"
                :allow-empty="false"
                @select="actionGetParent2"
                >
            </Multiselect>
            <div>Note: Edit name and code checklist for Level 1</div>
        </div> -->
      <!-- </div> -->
      <div class="col-md-6 text-center">
        <!-- <div class="row" v-if="itemDetail.job_id">
          <label class="col-md-5 form-label"
            >Level 2:</label>
            <Multiselect
                v-model="itemDetail.job_id_lv2"
                :options="listParent2 || []"
                :placeholder="$t('SELECT_ONE')"
                deselect-label="Can't remove this value"
                track-by="label"
                label="label"
                :searchable="false"
                :allow-empty="false"
                >
            </Multiselect>
        </div> -->
      </div>
    </div>

    <div class="row col-md-12 p-b-15">
    </div>
    <template #modalActions="modalActionProps">
      <button id="add_btn" type="submit" class="btn btn-primary ng-binding" style="width: 175px; text-align: center;" @click="confirmEditChecklist(modalActionProps)">
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
