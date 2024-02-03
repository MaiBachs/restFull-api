<script setup>
import { defineProps, computed, ref } from "vue";
import { defineEmits } from "vue";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import axios from "axios";
import { notify } from "@kyvg/vue3-notification";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import "vue3-timepicker/dist/VueTimepicker.css";
import { getlistPolicy }  from './../../store/getlistPolicy'
import moment from 'moment'

const listfillPolicy = getlistPolicy();
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

  
});
const itemDetail = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
//newDate
// const fromDate = ref(new Date());
// const toDate = ref(new Date());
// function formatDate(date) {
//   const day = date.getDate();
//   const month = date.getMonth() + 1;
//   const year = date.getFullYear();
//   return `${day}/${month}/${year}`;
// }
const isSaving = ref(false);
const isDateRangeValid = () => {
  if (itemDetail.value.start_date > itemDetail.value.end_date) {
    notify({
      type: 'error',
      title: 'Date Range Error',
      text: 'The "From Date" must be less than the "To Date".',
      duration: 10000,
    });
    return false;
  }
  return true;
};
async function confirmEditPolicy(action) {
  // console.log(itemDetail.value.end_date);
  itemDetail.value.start_date = moment(itemDetail.value.start_date).format("MM/DD/yyyy");
  itemDetail.value.end_date = moment(itemDetail.value.end_date).format("MM/DD/yyyy");
  console.log(itemDetail.value.start_date);
  console.log(itemDetail.value.end_date);

  if (!isDateRangeValid()) {
    return;
  }
  loaddingStore.loading = true;
  try {
    isSaving.value = true;
    // console.log(itemDetail.value.end_date);
    itemDetail.value.start_date = moment(itemDetail.value.start_date).format("MM/DD/yyyy");
    itemDetail.value.end_date = moment(itemDetail.value.end_date).format("MM/DD/yyyy");
    const response = await axios.put("/api/sale-bts-config/edit", {
      name: itemDetail.value.name,
      id: itemDetail.value.id,
      content: itemDetail.value.content,
      status: itemDetail.value.status,
      sale_policy_id: itemDetail.value.sale_policy_id,
      start_date: moment(itemDetail.value.start_date).format("DD/MM/yyyy"),
      end_date: moment(itemDetail.value.end_date).format("DD/MM/yyyy") !== 'Invalid date' ? moment(itemDetail.value.end_date).format("DD/MM/yyyy") : "",
    });
    isSaving.value = false;
    if (response && response.data.status && response.data.data != null) {
      notify({
        type: "success",
        title: "Edit Successful",
        text: response.data.status.message,
        duration: 10000,
      });
      loaddingStore.loading = false;
      action.onClose();
      emit("clearValue");
    } else {
      notify({
        type: "error",
        title: "Edit Error",
        text: response.data.status.message,
        duration: 10000,
      });
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
    itemDetail.value.name !== null &&
    itemDetail.value.content !== null &&
    itemDetail.value.status !== null &&
    itemDetail.value.start_date !== null &&
    itemDetail.value.time_from !== null &&
    itemDetail.value.time_to !== ""
  );
});
const optionsStatus = [
  {
    title: "Active",
    value: "1",
  },
  {
    title: "Inactive",
    value: "0",
  },
];

</script>
<template>
  <popupModal modal-id="exampleModalEditBTS" :title="$t('EDIT_BTS')">
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:</label>
          <input
            v-model="itemDetail.br_code"
            class="form-control-input col-md-7"
            type="text"
            readonly
            style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end; margin: 0 auto;min-height: calc(var(--ms-border-width, 0.4px) * 2 + var(--ms-font-size, 1rem) *
                  var(--ms-line-height, 1.375) + var(--ms-py, 0.5rem) * 2);
              outline: none;
              position: relative;
              width: 300px;
              padding-left: 26px;
              font-size: medium;
              font-weight: 500;
            "
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("BTS_CODE") }}:</label>
          <input
            v-model="itemDetail.bts_code"
            class="form-control-input col-md-7"
            type="text"
            readonly
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
              font-weight: 500;
            "
          />
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("POLICY") }}:</label>
          <Multiselect
                v-model="itemDetail.sale_policy_id"
                :options="listfillPolicy.listgetPolicy || []"
                track-by="name"
                :placeholder="$t('SELECT_ONE')"
                style="width: 300px">
          </Multiselect>
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("FROM_DATE") }}:</label>
          <VueDatePicker
            v-model="itemDetail.start_date"
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
          <label class="col-md-5 form-label">{{ $t("TO_DATE") }}:</label>
          <VueDatePicker
            v-model="itemDetail.end_date"
            auto-apply
            format="dd-MM-yyyy"
            style="width: 322px"
          ></VueDatePicker>
        </div>
      </div>
      <div class="col-md-6 text-center"></div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("STATUS") }}:</label>
          <Multiselect
            v-model="itemDetail.status"
            deselect-label="Can't remove this value"
            track-by="title"
            label="title"
            :placeholder="$t('SELECT_ONE')"
            :options="optionsStatus"
            :searchable="false"
            :allow-empty="false"
            style="width: 300px"
          >
          </Multiselect>
        </div>
      </div>
    </div>
    <template #modalActions="modalActionProps">
      <button
        type="submit"
        class="btn btn-primary ng-binding"
        style="width: 175px; text-align: center"
        @click="confirmEditPolicy(modalActionProps)"
        :disabled="!isConfirmEnabled"
      >
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
