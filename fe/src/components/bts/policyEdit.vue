<script setup>
import { defineProps, computed, ref, reactive } from "vue";
import { defineEmits } from "vue";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import axios from "axios";
import { notify } from "@kyvg/vue3-notification";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import VueTimepikcerEsm from "vue3-timepicker";
import "vue3-timepicker/dist/VueTimepicker.css";
import moment from 'moment'
import { getlistPolicy }  from './../../store/getlistPolicy'
const listfillPolicy = getlistPolicy();

// import getDeleteSaletime from "@/components/bts/getDeleteSaletime.vue";
const edit_sale_time_configs = ref ([]);
const delete_sale_time_configs = ref ([]);
const add_sale_time_configs = ref ([]);
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
const ListSaleTime = computed({
  get() {
    return props.ListSaleTime;
  },
  set(value) {
    emit("update:ListSaleTime", value);
  },
});
//newDate
// const fromDate = ref(new Date());
// const toDate = ref(new Date());
function formatDate(date) {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}
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
const isTimeRangeValid = () => {
  if (time.time_from >= time.time_to) {
    notify({
      type: 'error',
      title: 'Time Range Error',
      text: 'The "From Time" must be less than the "To Time".',
      duration: 10000,
    });
    return false;
  }
  return true;
};
const isCheckSaleTime = computed(() => {
  const saleTimeCount = ListSaleTime.value.length;
  const deleteConfigCount = delete_sale_time_configs.value.length;

  if (saleTimeCount === deleteConfigCount) {
    notify({
      type: 'error',
      title: 'Sale Time Error',
      text: 'Must keep at least 1 sale time',
      duration: 10000,
    });
    return false;
  }
  return true;
});
async function confirmEditPolicy(action) {
  itemDetail.value.start_date = moment(itemDetail.value.start_date).format("MM/DD/yyyy");
    itemDetail.value.end_date = moment(itemDetail.value.end_date).format("MM/DD/yyyy");
  if (!isDateRangeValid()) {
    return;
  }
  if (!isCheckSaleTime.value) { 
    return;
  }

  loaddingStore.loading = true;
  try {
    itemDetail.value.start_date = moment(itemDetail.value.start_date).format("MM/DD/yyyy");
    itemDetail.value.end_date = moment(itemDetail.value.end_date).format("MM/DD/yyyy");
    isSaving.value = true;
    const response = await axios.put("/api/map-sale-policy/edit", {
      name: itemDetail.value.name,
      id: itemDetail.value.id,
      content: itemDetail.value.content,
      status: itemDetail.value.status,
      start_date: moment(itemDetail.value.start_date).format("DD/MM/yyyy"),
      end_date: moment(itemDetail.value.end_date).format("DD/MM/yyyy") !== 'Invalid date' ? moment(itemDetail.value.end_date).format("DD/MM/yyyy") : "",
      add_sale_time_configs:add_sale_time_configs.value,
      edit_sale_time_configs:edit_sale_time_configs.value,
      delete_sale_time_configs:delete_sale_time_configs.value
    });
    isSaving.value = false;
    if (response && response.data.status && response.data.status.message) {
      response.data.start_date = formatDate(new Date(response.data.start_date));
      response.data.end_date = formatDate(new Date(response.data.end_date));
      notify({
        type: "error",
        title: "Edit Error",
        text: response.data.status.message,
        duration: 10000,
      });
      loaddingStore.loading = false;
    } else {
      notify({
        type: "success",
        title: "Edit Successful",
        duration: 10000,
      });
      action.onClose();
      emit("clearValue");
      listfillPolicy.actionGetlistPolicy()
      loaddingStore.loading = false;
      //cleardata
      idsToDelete.value = [];
      delete_sale_time_configs.value = [];
      edit_sale_time_configs.value = [];
      add_sale_time_configs.value = [];
    }
  } catch (error) {
    console.error(error);
    isSaving.value = false;
    loaddingStore.loading = false;
  }
}
const isConfirmEnabled = computed(() => {
  return (
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


var editItem = computed({
  get() {
    return props.ListSaleTime || {}
  },
  set(value) {
    emit('update:modelValue', value)
  }
})
const copyOfListSaleTime = [...props.ListSaleTime];
const time = reactive({
  time_from : "",
  time_to : ""
});
const addTimeRecord = () => {
  if (!isTimeRangeValid()) {
    return;
  }
    if (time.time_from && time.time_to) {
    const time_from = time.time_from.HH + ":" + time.time_from.mm;
    const time_to = time.time_to.HH + ":" + time.time_to.mm;
    console.log(time_from);
    console.log(time_to);
    const newRecord ={
      "time_from": time.time_from,
      "time_to": time.time_to
    }
    emit("update:modelValue", {name: itemDetail.value.name, id: itemDetail.value.id, content: itemDetail.value.content, status: itemDetail.value.status, start_date: itemDetail.value.start_date, end_date: itemDetail.value.end_date  ,add_sale_time_configs: copyOfListSaleTime });
    add_sale_time_configs.value.push(newRecord)
    copyOfListSaleTime.push(newRecord);
    editItem.value.push({...newRecord});
    console.log(add_sale_time_configs);
  } else {
    
    notify({
      type: 'error',
      // title: 'Time Error',
      text: 'Both "Time From" and "Time To" must be filled.',
      duration: 10000,
    });
  }
};

const clearTime = () => {
  time.time_from = null; 
  time.time_to = null; 
};
let idsToDelete = [];
const getDeleteSaleTime = (id) => {
  for (const item of editItem.value) {
    if (id === item.id) {
      console.log(item.id);
      // const itemIndex = editItem.value.indexOf(item);
      // if (itemIndex === editItem.value.length - 1) {
      //   // Kiểm tra nếu item là phần tử cuối cùng, không làm gì cả
      //   return;
      // }
      idsToDelete.push({ id: item.id });
      console.log(idsToDelete);
    }
  }
  delete_sale_time_configs.value = idsToDelete;
}
console.log(delete_sale_time_configs.value);
const handleDeleteRow = (item, event) => {
  getDeleteSaleTime(item.id);
  deleteRow(event);
};
// function deleteRow(event) {
//   var row = event.target.closest('tr');
//   if (row) {
//     var table = row.closest('table');
//     if (table) {
//       var rowIndex = row.rowIndex;
//       if (rowIndex > 0 && rowIndex < table.rows.length - 1) {
//         table.deleteRow(rowIndex);
//       } else if (rowIndex === table.rows.length - 1) {
//         alert("Must keep at least 1 sale time");
//       }
//     }
//   }
// }
function deleteRow(event) {
  var row = event.target.closest('tr');
  if (row) {
    var table = row.closest('table');
    if (table) {
      var rowIndex = row.rowIndex;
      if (rowIndex >= 0) {
        table.deleteRow(rowIndex);
      }
    }
  }
}
function closeModal() {
      delete_sale_time_configs.value = [];
      edit_sale_time_configs.value = [];
      add_sale_time_configs.value = [];
      idsToDelete = [];
      console.log(add_sale_time_configs);
      console.log(delete_sale_time_configs);
      console.log(idsToDelete);
}
</script>
<template>
  <popupModal modal-id="exampleModalEditlPolicy" :title="$t('EDIT_POLICY')" @close-modal="closeModal">
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("POLICY") }}:</label>
          <input
            v-if="itemDetail.name !== 'Bts normal'"
            v-model="itemDetail.name"
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
            font-weight: 500;"
          />
          <input
            v-else
            :value="itemDetail.name"
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
            font-weight: 500;"
            readonly
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("DESCRIPTION") }}:</label>
          <input
            v-model="itemDetail.content"
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
              font-weight: 500;
            "
          />
        </div>
      </div>
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
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("FROM_DATE") }}:</label>
          <VueDatePicker
            v-model="itemDetail.start_date"
            auto-apply
            format="dd/MM/yyyy"
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
            v-if="itemDetail.name !== 'Bts normal'"
            v-model="itemDetail.end_date"
            auto-apply
            format="dd/MM/yyyy"
            style="width: 322px"
          ></VueDatePicker>
          <VueDatePicker
            v-else
            :value="itemDetail.end_date"
            auto-apply
            format="dd/MM/yyyy"
            style="width: 322px"
            readonly
          ></VueDatePicker>
        </div>
      </div>
      <div class="col-md-6 text-center"></div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("SALE_TIME") }}:</label>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label">{{ $t("TIME_FROM") }}:</label>
        <div class="col-sm-6">
          <VueTimepikcerEsm
            v-model="time.time_from"
            auto-apply
            format="HH:00"
            @clear = "clearTime"
          ></VueTimepikcerEsm>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label">{{ $t("TIME_TO") }}:</label>
        <div class="col-sm-6">
          <VueTimepikcerEsm
            v-model="time.time_to"
            auto-apply
            format="HH:00"
            @clear = "clearTime"
          ></VueTimepikcerEsm>
        </div>
        <button
          id="add_btn"
          type="button"
          class="btn btn-primary ng-binding"
          @click="addTimeRecord"
          style="width: 30px; height: 30px; font-size: 18px; padding: 0">
          +
        </button>
        <table
          class="table table-bordered table-hover" 
          id="myTable">
          <thead
            class="table-success"
            style="background-color: #24695c; color: #fff">
            <tr>
              <!-- <th scope="col">ID</th> -->
              <th scope="col">{{ $t("TIME_FROM") }}</th>
              <th scope="col">{{ $t("TIME_TO") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in editItem" :key="item">
              <!-- <td>{{ item.id }}</td> -->
              <td>{{ item.time_from }}</td>
              <td>{{ item.time_to }}</td>
              <td>
                <div class="buttons">
                  <button
                    style="background-color: rgb(188, 22, 22) !important"
                    class="btn btn-primary btn-xs"
                    @click="handleDeleteRow(item, $event)" >
                    <i class="icofont icofont-ui-delete"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
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
