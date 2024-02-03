<script setup>


import axios from 'axios';
import { notify } from "@kyvg/vue3-notification";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from './../../store/LoaddingStore'
import { defineProps, defineEmits, computed} from "vue"
import { getChannelType} from "@/store/getChannelType";
const getlistPlan = getChannelType()
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
async function deleteChannel (action)  {
  loaddingStore.loading = true;
  try {
    const response = await axios.post("/api/plan/delete", { plan_id: item.value.plan_id });
    console.log(item.value.plan_id);
    if(response.data) {
      notify({
        type: "success",
        title: "Delete Success",
        text: response.data,
        duration: 10000,
      });
      loaddingStore.loading = false;
    } else {
      notify({
        type: "success",
        title: "Delete Success",
        text: 'Success',
        duration: 10000,
      });
      
      loaddingStore.loading = false;
    }
    console.log(response);
    getlistPlan.actionGetListFillPlan()
    action.onClose();
    emit('clearValue')
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
  <popupModal modal-id="exampleModalDelete" :title="$t('DELETE_CHANNEL')"  @close-modal="closeModal">
    <div class="row col-md-12 p-b-15">
        <div class="row" style="margin: 0px auto; text-align: center;">
            <h5>{{ $t("QUESTION_DELETE") }}</h5>
        </div>
    </div>
    <template #modalActions="modalActionProps">
      <button type="submit" class="btn btn-primary ng-binding" style="width: 175px; text-align: center;" @click="() => deleteChannel(modalActionProps)">
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
