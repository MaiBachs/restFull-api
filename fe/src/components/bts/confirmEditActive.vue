<script setup>


import axios from 'axios';
import { notify } from "@kyvg/vue3-notification";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from './../../store/LoaddingStore'
import { defineProps, defineEmits, computed} from "vue"
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
async function deleteChannel(action) {
  loaddingStore.loading = true;
  try {
    const newStatus = item.value.status === 1 ? 0 : 1; 
    const response = await axios.put("/api/sale-bts-config/set-status", {
      id: item.value.id,
      status: newStatus,
    });

    if (response.data) {
      notify({
        type: "success",
        title: "Change Success",
        duration: 10000,
      });
    } else {
      notify({
        type: "Error",
        title: "Change Error",
        text: "Error",
        duration: 10000,
      });
    }
    action.onClose();
    emit("clearValue");
  } catch (error) {
    notify({
      type: "Error",
      title: "Change Error",
      text: Error,
      duration: 10000,
    });
  } finally {
    loaddingStore.loading = false;
  }
}

</script>
<template>
  <popupModal modal-id="exampleModalConfirmActive" :title="$t('BTS')"  @close-modal="closeModal">
    <div class="row col-md-12 p-b-15">
        <div class="row" style="margin: 0px auto; text-align: center;">
            <h5>{{ $t("QUESTION_ACTIVE") }}</h5>
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