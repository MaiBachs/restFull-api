<script setup>

import Multiselect from "@vueform/multiselect"
import {ref} from "vue"
import {reactive, defineEmits} from "vue"
import {getParent} from "@/store/getParent";
import axios from "axios";
import { notify } from "@kyvg/vue3-notification";
import popupModal from '../../app/popupModal.vue'
import { useLoaddingStore } from './../../store/LoaddingStore'
import { getChannelType} from "@/store/getChannelType";

const getlistLevle1 = getChannelType()
const loaddingStore = useLoaddingStore()
const listParent = getParent();
const searchData =  reactive({
  job_id: "",
  parent_id: "",
  name: "",
  code: "",
  parent_name: "",
  result_data_type: "",
  job_id_lv2: "",
});

const emit = defineEmits(['update:modelValue'])

const listParent2 = ref({})
const actionGetParent2 = async  (parentId) =>{
  const Parent2 = await axios.post('/api/job/fill-check-list-by-parentId',
      {
        parent_id: parentId
      });
  listParent2.value = []
  if(Parent2.data && Parent2.data.length) {
    listParent2.value  = Parent2.data.map(function (o) {
      return {
        value: o.job_id,
        label: o.name,
      };
    })
  }
}

//getLEVEL1
// const actionGetParent = async  (parentId) =>{
//   console.log(parentId);
//   const Parent = await axios.post('/api/job/fill-check-list-levle-1',
//       {
//         parent_id: parentId
//       });
// console.log(actionGetParent);
//   if(Parent.data && Parent.data.length) {
//     listParent.listParent  = Parent.data.map(function (o) {
//       return {
//         value: o.job_id,
//         label: o.name,
//       };
//     })
//   }
// }


const saveaddChecklist = async (action) =>{
  loaddingStore.loading = true;
  const response  = await axios.post('/api/job/add-new-checklist', {
    name:  searchData.name,
    parent_id:  searchData.job_id_lv2 ? searchData.job_id_lv2 : searchData.job_id,
    code: searchData.code,
    result_data_type: searchData.result_data_type,
    parent_name: searchData.parent_name,
    // job_id: searchData.job_id,
    job_id_lv2: searchData.job_id_lv2,
  });
  if(searchData.job_id_lv2 == null){
    response.parent_id= searchData.job_id;
  }else{
    response.parent_id= searchData.job_id_lv2;
  }
  if(response && response.data.status.message && response.data.status.message.length > 0) {
    notify({
      type: 'error',
      title: "Add Error",
      text: response.data.status.message,
      duration: 10000,
    });
    loaddingStore.loading = false;
  } else {
    notify({
      type: 'success',
      title: "Add successful",
      // text: response.data.status,
      duration: 10000,
    });
    getlistLevle1 .actionGetListLevle1()
    action.onClose();
    emit('clearValue')
  }
};

function closeModal() {
  searchData.value = []
  searchData.name = null
  searchData.code = null
  searchData.job_id = null
  searchData.job_id_lv2 = null
}

</script>

<template>
  <popupModal modal-id="exampleModaladdChecklist" :title="$t('ADD_NEW_CHECKLIST')" @close-modal="closeModal">
    <div class="row col-md-12 p-b-15" >
      <div class="col-md-6 text-center" style="padding-right: 30px">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.NAME") }}:</label>
          <input
              v-model="searchData.name"
              style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px);padding-left: 26px;font-size: initial;font-weight: 405;">
        </div>
      </div>
      <div class="col-md-6 text-center" >
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.CODE") }}:</label>
          <input
              v-model="searchData.code"
              style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px);padding-left: 26px;font-size: initial;font-weight: 405;">
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center" style="padding-right: 39px">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("LEVEL_1") }}:</label>
          <Multiselect
              v-model="searchData.job_id"
              :options="listParent.listParent || []"
              :placeholder="$t('SELECT_ONE')" 
              @select="actionGetParent2(searchData.job_id)" 
              style="width: 300px;"></Multiselect>
          <div>{{ $t("NOTE_CREATE_LEVEL_1") }}</div>
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15" v-if="searchData.job_id">
      <div class="col-md-6 text-center" style="padding-right: 39px">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("LEVEL_2") }}:</label>
          <Multiselect
              v-model="searchData.job_id_lv2"
              :options="listParent2 || []"
              :placeholder="$t('SELECT_ONE')"
              style="width: 300px;"
          ></Multiselect>
        </div>
      </div>
      <div class="col-md-6 text-center">
      </div>
    </div>
    <div class="row col-md-12 p-b-15" v-if="searchData.parent_id">
      <div class="col-md-6 text-center">
      </div>
    </div>
    <template #modalActions="modalActionProps">
      <button :disabled="!searchData.name || !searchData.code" type="submit" class="btn btn-primary ng-binding" style="width: 175px; text-align: center;" @click="saveaddChecklist(modalActionProps)">{{ $t("ADD") }} </button>
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
