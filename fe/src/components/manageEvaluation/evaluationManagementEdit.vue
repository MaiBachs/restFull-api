<script setup>
import { defineProps, computed, ref } from "vue";
import { defineEmits } from "vue";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import axios from "axios";
import { notify } from "@kyvg/vue3-notification";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import moment from "moment";
import { useAuditEvalutionStore } from "../../store/AuditEvalutionStore";
const auditEvaluation = useAuditEvalutionStore();
const loaddingStore = useLoaddingStore();
const emit = defineEmits(["onOk", "update:modelValue"]);
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  options: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  ListSaleTime: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  itemAdd: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  ReasonVisitPlan: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  formattedStatus: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});
const itemDetail = computed({
  get() {

    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});


const ReasonVisitPlan = computed({
  get() {
    return props.ReasonVisitPlan;
  },
  set(value) {
    emit("update:ReasonVisitPlan", value);
  },
});

function formatDate(date) {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}
const isSaving = ref(false);
async function approveEditEvaluationManagement(action) {
  loaddingStore.loading = true;
  try {
    itemDetail.value.scheduled_date = moment(itemDetail.value.scheduled_date).format(
      "MM/DD/yyyy"
    );
    isSaving.value = true;
    const response = await axios.post("/api/evaluation-manage/edit-action-plan", {
      check_reject_or_approve: 1,
      auditor_id: itemDetail.value.auditor_id,
      visit_plan_id: ReasonVisitPlan.value[0].action_plan.id,
      scheduled_date: moment(itemDetail.value.scheduled_date).format("DD/MM/yyyy"),
      status_action_plan: ReasonVisitPlan.value[0].action_plan.status,
      comment: itemDetail.value.comment,
    });
    isSaving.value = false;
    if (response && response.data.status && response.data.status.message) {
      response.data.scheduled_date = formatDate(new Date(response.data.scheduled_date));
      notify({
        type: "error",
        title: "Approve Error",
        text: response.data.status.message,
        duration: 10000,
      });
      loaddingStore.loading = false;
    } else {
      notify({
        type: "success",
        title: "Approve Successful",
        duration: 10000,
      });
      action.onClose();
      emit("clearValue");
      loaddingStore.loading = false;
    }
  } catch (error) {
    console.error(error);
    isSaving.value = false;
    loaddingStore.loading = false;
  }
}
async function rejectEditEvaluationManagement(action) {
  loaddingStore.loading = true;
  try {
    itemDetail.value.scheduled_date = moment(itemDetail.value.scheduled_date).format(
      "MM/DD/yyyy"
    );
    isSaving.value = true;
    const response = await axios.post("/api/evaluation-manage/edit-action-plan", {
      check_reject_or_approve: -1,
      auditor_id: itemDetail.value.auditor_id,
      visit_plan_id: ReasonVisitPlan.value[0].action_plan.id,
      scheduled_date: moment(itemDetail.value.scheduled_date).format("DD/MM/yyyy"),
      status_action_plan: ReasonVisitPlan.value[0].action_plan.status,
      comment: itemDetail.value.comment,
    });
    isSaving.value = false;
    if (response && response.data.status && response.data.status.message) {
      response.data.scheduled_date = formatDate(new Date(response.data.scheduled_date));
      notify({
        type: "error",
        title: "Reject Error",
        text: response.data.status.message,
        duration: 10000,
      });
      loaddingStore.loading = false;
    } else {
      notify({
        type: "success",
        title: "Reject Successful",
        duration: 10000,
      });
      action.onClose();
      emit("clearValue");
      loaddingStore.loading = false;
    }
  } catch (error) {
    console.error(error);
    isSaving.value = false;
    loaddingStore.loading = false;
  }
}
const isConfirmEnabled = computed(() => {
  return (
    itemDetail.value.auditor_id !== null &&
    itemDetail.value.scheduled_date !== null
  );
});

const FormattedStatus = computed(() => {
  switch (itemDetail.value.status_evaluation) {
    case 1:
      return "Done";
    case 2:
      return "Need Action Plan";
    case 3:
      return "In Process";
    default:
      return "Unknown Status";
  }
})

// const option = [
//   {
//     title: "Done",
//     value: "0",
//   },
//   {
//     title: "Need Action Plan",
//     value: "1",
//   },
// ];

function closeModal() {}
</script>
<template>
  <popupModal
    modal-id="exampleEvaluationManagementEdit"
    :title="$t('EDIT_EVALUATION_MANAGEMENT')"
    @close-modal="closeModal"
  >
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("AUDITTORS") }}:</label>
          <Multiselect
            v-model="itemDetail.auditor_id"
            :options="auditEvaluation.listAuditor || []"
            :placeholder="$t('SELECT_ONE')"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 12px;
              font-size: medium;
              font-weight: 400;
            "
          ></Multiselect>
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("EVALUATION") }}:</label>
          <input
            readonly
            v-model="itemDetail.evaluation_name"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("STATUS") }}:</label>
          <input
            readonly
            v-model="FormattedStatus"
            class="form-control-input col-md-7"
            type="text"
            style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end;margin: 0 auto;min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:</label
          >
          <input
            readonly
            v-model="itemDetail.branch_code"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("SCORE") }}:</label>
          <input
            readonly
            v-model="itemDetail.score"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("ACTION_PLAN") }}:</label>
          <input
            readonly
            v-model="itemDetail.action_plan"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:</label
          >
          <input
            readonly
            v-model="itemDetail.type_channel"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >{{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE") }}:</label
          >
          <input
            readonly
            v-model="itemDetail.channel_code"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("SCHEDULE_DATE") }}:</label>
          <VueDatePicker
            v-model="itemDetail.scheduled_date"
            auto-apply
            format="dd-MM-yyyy"
            style="width: 322px"
          ></VueDatePicker>
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >{{ $t("EVALUATION_DATE") }}:</label
          >
          <VueDatePicker
            readonly
            v-model="itemDetail.visit_time"
            auto-apply
            format="dd-MM-yyyy"
            style="width: 322px"
          ></VueDatePicker>
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("NOTE") }}:</label>
          <input
            readonly
            v-model="itemDetail.check_list_result_comment"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >{{ $t("COMMENT") }}:</label
          >
          <input
            v-model="itemDetail.comment"
            class="form-control-input col-md-7"
            type="text"
            style="
              align-items: center;
              box-sizing: border-box;
              cursor: pointer;
              display: flex;
              justify-content: flex-end;
              margin: 0 auto;
              min-height: calc(
                var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2
              );
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 400;
            "
          />
        </div>
      </div>
    </div>
    <template #modalActions="modalActionProps">
      <button
        type="submit"
        class="btn btn-primary ng-binding"
        style="width: 175px; text-align: center"
        @click="approveEditEvaluationManagement(modalActionProps)"
        :disabled="!isConfirmEnabled"
      >
        {{ $t("APPROVE") }}
      </button>
      <button
        type="submit"
        class="btn ng-binding"
        style="width: 175px; text-align: center;background-color: #d9534f; color: #ffffff;"
        @click="rejectEditEvaluationManagement(modalActionProps)"
        :disabled="!isConfirmEnabled"
      >
        {{ $t("REJECT") }}
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
