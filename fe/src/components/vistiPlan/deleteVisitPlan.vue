<script setup>
import { defineProps, defineEmits } from "vue";
import axios from "axios";
import { i18n } from "../../main.js";
import { useLoaddingStore } from "../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import warningModal from "../common/warningModal.vue";
const loaddingStore = useLoaddingStore();
const emit = defineEmits(["onOk", "update:modelValue"]);
const props = defineProps({
  planToDeleteIds: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});

async function deleteVisitPlan(action) {
  try {
    loaddingStore.loading = true;
    const response = await axios.post("/api/visit-plan/delete-visit-plan", {
      visit_plan_id: props.planToDeleteIds,
    });
    notify({
      type: "success",
      title: i18n.global.t("VISIT_PLAN.LABEL.TITLE_DELETE_MESSAGE"),
      text: response.data.message,
      duration: 3000,
    });
    loaddingStore.loading = false;
    action.onClose();
    emit("clearValue");
  } catch (error) {
    loaddingStore.loading = false;
    notify({
      type: "error",
      title: i18n.global.t("VISIT_PLAN.LABEL.TITLE_DELETE_MESSAGE"),
      text: error,
      duration: 500,
    });
  }
}
</script>
<template>
  <warningModal modal-id="openWarningModal" :title="$t('VISIT_PLAN.LABEL.TITLE_DELETE_MESSAGE')" @close-modal="closeModal">
    <div class="modal-body text-center p-0">
          {{ $t("VISIT_PLAN.LABEL.CONFIRM") }}
    </div>
    <template #modalActions="modalActionProps">
      <button type="button" class="btn btn-danger" @click="deleteVisitPlan(modalActionProps)">
        {{ $t("DELETE") }}
      </button>
    </template>
  </warningModal>
 </template>
