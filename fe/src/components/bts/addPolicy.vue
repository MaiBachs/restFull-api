<script setup>
import {reactive, ref} from "vue";
import { useLoaddingStore } from './../../store/LoaddingStore'
import { notify } from "@kyvg/vue3-notification";
import axios from "axios"
import VueDatePicker from "@vuepic/vue-datepicker";
import VueTimepikcerEsm from "vue3-timepicker";
import { getlistPolicy }  from './../../store/getlistPolicy'
import "vue3-timepicker/dist/VueTimepicker.css";
import moment from "moment/moment";
// import { defineEmits } from "vue";
// const props = defineProps({
//   modelValue: {
//     type: Object,
//     required: true,
//     default: () => ({}),
//   },
// });
// const emit = defineEmits(["onOk", "update:modelValue"]);
const listfillPolicy = getlistPolicy();
const loaddingStore = useLoaddingStore()
// const fromDate = ref(new Date());
// const toDate = ref(new Date());
const fromDate = ref();
const toDate = ref();
const formData = reactive({
  name: "",
  start_date: "",
  end_date: "",
  content: "",
  add_sale_time_configs: [
    {
      time_from: "",
      time_to: "",
    }
  ]
});
function formatDate(date) {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}
const isDateRangeValid = () => {
  const fromDateValue = fromDate.value;
  const toDateValue = toDate.value;
  if (!fromDateValue) {
    notify({
      type: 'error',
      title: 'FromDate Error',
      text: 'The "From Date" must be filled.',
      duration: 10000,
    });
    return false;
  }
  if (!toDateValue) {
    return true;
  }
  if (fromDateValue > toDateValue) {
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
    // add_sale_time_configs.value = [];
    return false;
  }
  return true;
};
const addPolicy = async () =>{
  if (!isDateRangeValid()) {
    return;
  }
  if (add_sale_time_configs.value.length === 0) {
    notify({
      type: 'error',
      title: 'Time Error',
      text: 'Both "Time From" and "Time To" must be filled.',
      duration: 10000,
    });
    add_sale_time_configs.value = [];
    return;
  }
  loaddingStore.loading = true;
  formData.start_date = fromDate.value ? moment(fromDate.value).format("DD/MM/yyyy") : "";
  formData.end_date = toDate.value ? moment(toDate.value).format("DD/MM/yyyy") : "";
  console.log(formData)
  const response  = await axios.post('/api/map-sale-policy/add', {
    name: formData.name,
    start_date: formData.start_date,
    end_date: formData.end_date,
    content: formData.content,
    add_sale_time_configs: add_sale_time_configs.value
  });
  console.log(response)
  if(response.data.data === null) {
    notify({
      type: 'error',
      title: "Add Error",
      text: response.data.status.message,
      duration: 10000,
    });
    add_sale_time_configs.value = [];
    loaddingStore.loading = false;
  } else {
   const responseData = response.data.data;
   console.log(formatDate(new Date(responseData.start_date)));
    responseData.start_date = formatDate(new Date(responseData.start_date));
    responseData.end_date = formatDate(new Date(responseData.end_date));
    notify({
      type: 'success',
      title: "Add successful",
      text: response.data.status.message,
      duration: 10000,
    });
    add_sale_time_configs.value = [];
    listfillPolicy.actionGetlistPolicy()
    loaddingStore.loading = false;
  }
};
const time = reactive({
  time_from : "",
  time_to : ""
});
// var editItem = computed({
//   get() {
//     return props.modelValue || {}
//   },
//   set(value) {
//     emit('update:modelValue', value)
//   }
// })
// const delete_sale_time_configs = ref ([]);
const add_sale_time_configs = ref([]);
const addTimeRecord = () => {
  if (!isTimeRangeValid()) {
    return;
  }
  if (!time.time_to) {
    notify({
      type: 'error',
      title: 'Time Error',
      text: 'Please fill in "Time To" before adding a record.',
      duration: 10000,
    });
    return;
  }
  if (!time.time_from) {
    notify({
      type: 'error',
      title: 'Time Error',
      text: 'Please fill in "Time From" before adding a record.',
      duration: 10000,
    });
    return;
  }
  const newRecord ={
    "time_from": time.time_from,
    "time_to": time.time_to
  }
  add_sale_time_configs.value.push(newRecord)
  time.time_from = "";
  time.time_to = "";
};
// const getDeleteSaleTime = (id) => {
//   const idsToDelete = [];
//   for (const item of editItem.value) {
//     if (id == item.id) {
//       idsToDelete.push({
//         "id" : id,
//       });
//     }
//   }
//   delete_sale_time_configs.value = idsToDelete;
//   console.log(delete_sale_time_configs);
// }
// const handleDeleteRow = (item, event) => {
//   getDeleteSaleTime(item);
//   deleteRow(event);
// };
// function deleteRow(event) {
//   var row = event.target.closest('tr');
//   if (row) {
//     var table = row.closest('table');
//     if (table) {
//       var rowIndex = row.rowIndex;
//       if (rowIndex > 0) {
//         table.deleteRow(rowIndex);
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
      if (rowIndex > 0) {
        var timeFromCell = row.cells[0];
        var timeToCell = row.cells[1];
        var timeFrom = timeFromCell.textContent.trim();
        var timeTo = timeToCell.textContent.trim();
        table.deleteRow(rowIndex);
        for (var i = 0; i < add_sale_time_configs.value.length; i++) {
          console.log(add_sale_time_configs.value[i]);
          if (add_sale_time_configs.value[i].time_from === timeFrom && add_sale_time_configs.value[i].time_to === timeTo) {
            add_sale_time_configs.value.splice(i, 1);
            break; 
          }
        }
      }
    }
  }
}

</script>
<template>
  <div class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label"
        >{{ $t("POLICY") }}:<span style="font-weight: bold; color: red;">*</span></label>
        <div class="col-sm-6">
          <input v-model="formData.name" type="text" style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.0) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 100%;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px)" />
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label"
        >{{ $t("DESCRIPTION") }}:</label
        >
        <div class="col-sm-6">
          <input v-model="formData.content" type="text" style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.0) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 100%;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px)"/>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label">{{ $t("FROM_DATE") }}:<span style="font-weight: bold; color: red;">*</span></label>
        <div class="col-sm-6" >
          <VueDatePicker
              v-model="fromDate"
              auto-apply
              format="dd-MM-yyyy"></VueDatePicker>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label">{{ $t("TO_DATE") }}:</label>
        <div class="col-sm-6">
          <VueDatePicker
              v-model="toDate"
              auto-apply
              format="dd-MM-yyyy"></VueDatePicker>
        </div>
      </div>
    </div>
    <div class="col-md-12">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label">{{ $t("INFORMATION_SALE_TIME") }}:</label>
      </div>
    </div>
  </div>
  <div class="col-md-6">
    <div class="mb-3 row">
      <label class="col-sm-3 col-form-label">{{ $t("TIME_FROM") }}:<span style="font-weight: bold; color: red;">*</span></label>
      <div class="col-sm-6" >
        <VueTimepikcerEsm
            v-model="time.time_from"
            auto-apply
            format="HH:00"
            ></VueTimepikcerEsm>
      </div>
    </div>
  </div>
  <div class="col-md-6">
    <div class="mb-3 row">
      <label class="col-sm-3 col-form-label">{{ $t("TIME_TO") }}:<span style="font-weight: bold; color: red;">*</span></label>
      <div class="col-sm-6">
        <VueTimepikcerEsm
            v-model="time.time_to"
            auto-apply
            format="HH:00"></VueTimepikcerEsm>
      </div>
      <button
          id="add_btn"
          type="submit"
          class="btn btn-primary ng-binding"
          @click="addTimeRecord"
          style="width: 30px; height: 30px; font-size: 18px; padding: 0;"
      >
        +
      </button>
    </div>
  </div>
  <div class="col-md-6">
    <table v-if="add_sale_time_configs.length > 0"
          class="table table-bordered table-hover" 
          id="myTable">
          <thead
            class="table-success"
            style="background-color: #24695c; color: #fff">
            <tr>
              <th scope="col">{{ $t("TIME_FROM") }}</th>
              <th scope="col">{{ $t("TIME_TO") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in add_sale_time_configs" :key="item">
              <td>{{ item.time_from }}</td>
              <td>{{ item.time_to }}</td>
              <td>
                <div class="buttons">
                  <button
                    style="background-color: rgb(188, 22, 22) !important"
                    class="btn btn-primary btn-xs"
                    @click="deleteRow($event)"
                  >
                    <i class="icofont icofont-ui-delete"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

  </div>
  
  <div class="row col-xs-12" >
    <button 
    id="add_btn" 
    type="submit" 
    class="btn btn-primary ng-binding" 
    style="width: 175px; 
    margin: 0px auto; 
    text-align: center;" 
    @click="addPolicy"
    :disabled="!formData.name || !fromDate || !add_sale_time_configs.length">
    {{ $t("ADD_POLICY") }} 
  </button>
  </div>
</template>

