<script setup>


import axios from 'axios';
import { notify } from "@kyvg/vue3-notification";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from './../../store/LoaddingStore'
import { defineProps, defineEmits, computed} from "vue"
// import i18n from '@/i18n';

const emit = defineEmits(['onOk', 'update:modelValue'])
const loaddingStore = useLoaddingStore()
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => {[]}
  }
  
});
const item = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value)
  }
},);
console.log(item.value)
async function deleteItemChecklist (action)  {
  loaddingStore.loading = true;
  try {
    const response = await axios.post("/api/job/delete-checklist", { job_id: item.value.job_id });
    console.log(item.value.job_id);
    if(response.status == 200) {
      notify({
        type: "success",
        text: "Delete Success",
        duration: 10000,
      });
      loaddingStore.loading = false;
    }
    action.onClose();
    emit('clearValue') //clear thong tin va search lai
    loaddingStore.loading = true;
  } catch (error) {
    loaddingStore.loading = false;
    notify({
      type: "error",
      title: "ERROR DELETE",
      text: error,
      duration: 10000,
    });
  }

}

</script>
<template>
  <popupModal modal-id="exampleModalDeleteChecklist" :title="$t('DELETE_CHECKLIST')"  @close-modal="closeModal">
    <div class="row col-md-12 p-b-15">
        <div class="row" style="margin: 0px auto; text-align: center;">
            <h6 v-if="!item.checkMessage">{{ $t("QUESTION_DELETE") }}</h6>
            <h6 v-if="item.checkMessage">{{$t("CHECKLIST_IS_PARENT_LEVEL")}}</h6>
          
        </div>
        <!-- v-if="props.listLevel1.some(list => list.value === item.job_id)" -->
    </div>
    <template #modalActions="modalActionProps">
      <button type="submit" class="btn btn-primary ng-binding" style="width: 175px; text-align: center;" @click="() => deleteItemChecklist(modalActionProps)">
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
