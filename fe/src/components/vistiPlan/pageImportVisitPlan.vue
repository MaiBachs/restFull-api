<script setup>
import { ref } from "vue";
import Multiselect from "@vueform/multiselect";
import { useItemConfigStore } from "./../../store/ItemConfigStore";
import * as XLSX from "xlsx";
import { notify } from "@kyvg/vue3-notification";
import axios from "axios";
import { useLoaddingStore } from "./../../store/LoaddingStore";

const loaddingStore = useLoaddingStore();
const itemConfigStore = useItemConfigStore();
const quantitySuccess = ref("");
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref("");
const listDataFile = ref("");
const inputFile = ref(null);
const fileData = ref("");
const totalRecord = ref();
const pageImport = ref(1);
const listDataFileShow = ref([]);
const type = ref("");

const changeType = () => {
  listDataFile.value = [];
};

const options = [
  {
    title: "Import",
    value: "1",
  },
  {
    title: "Remove",
    value: "2",
  },
];
const previewFiles = async () => {
  listDataFile.value = [];
  listDataFileShow.value = [];
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  fileData.value = inputFile.value.files[0];
  const file = fileData.value; // Lấy tệp đầu tiên nếu có nhiều tệp
  if (file) {
    var reader = new FileReader();
    reader.onload = () => {
      var fileData = reader.result;
      var wb = XLSX.read(fileData, {
        type: "binary",
        cellText: false,
        cellDates: true,
      });
      listDataFile.value = XLSX.utils.sheet_to_json(
        wb.Sheets[wb.SheetNames[0]],
        {
          header: 0,
          raw: false,
          dateNF: "DD/MM/YYYY",
        }
      );
      listDataFileShow.value = listDataFile.value = XLSX.utils.sheet_to_json(
        wb.Sheets[wb.SheetNames[0]],
        {
          header: 0,
          raw: false,
          dateNF: "DD/MM/YYYY",
        }
      );

      if (listDataFile.value && listDataFile.value.length) {
        totalRecord.value = listDataFile.value.length;
        myCallback();
      }
    };
    reader.readAsBinaryString(file);
  }
  inputFile.value.value = null;
  quantitySuccess.value = null;
  fileNameError.value = null;
};

function myCallback() {
  var i = (pageImport.value - 1) * 10;
  var j = pageImport.value * 10;
  listDataFile.value = [];
  for (i; i < j; i++) {
    if (listDataFileShow.value[i]) {
      listDataFile.value.push(listDataFileShow.value[i]);
    }
  }
}

const importVisitPlan = async () => {
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  try {
    const formData = new FormData();
    formData.append("file", fileData.value);
    if (type.value == 1) {
      formData.append("actionType", "ADD");
    } else if (type.value == 2) {
      formData.append("actionType", "DELETE");
    }

    loaddingStore.loading = true;
    const response = await axios.post("/api/visit-plan/import", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    if(response.data.fileImportResultName) {
      errorMessageFlag.value = true
      quantitySuccess.value = response.data.errorMessageImport
      fileNameError.value = response.data.fileImportResultName
    } else {
      listDataFile.value = [];
      successMessageFlag.value = true
    }
    
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    console.log(error);
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 500,
    });
  }
};

const downloadFileTemplate = async () => {
  itemConfigStore.downloadFile(`visit_plan_template.xls`);
};

const downloadErrorImmportFile = () => {
  itemConfigStore.downloadFileError(fileNameError.value);
};
</script>
<template>
  <div class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label">{{
          $t("TYPE_OF_ACTION")
        }}</label>
        <div class="col-sm-6">
          <multiselect
            v-model="type"
            deselect-label="Can't remove this value"
            track-by="title"
            label="title"
            placeholder="Select one"
            :options="options"
            v-on:change="changeType()"
            :searchable="false"
            :allow-empty="false"
          >
          </multiselect>
        </div>
      </div>
    </div>
    <div class="col-md-4 text-center">
      <div class="mb-3 row">
        <button
          class="btn btn-light"
          style="width: 100%; color: #24695c"
          @click="downloadFileTemplate"
        >
          <i class="fa fa-download"></i>
          {{ $t('VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE') }}
        </button>
      </div>
    </div>
  </div>
  <div v-if="type" class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label text-left"
          >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
        >
        <div v-if="!fileData.name" class="col-sm-8 col-form-label">
          <input
            id="file"
            ref="inputFile"
            accept=".xls, .xlsx"
            type="file"
            @change="previewFiles"
          />
        </div>
        <div v-if="fileData.name" class="col-sm-8 col-form-label">
          <input
            id="file"
            ref="inputFile"
            accept=".xls, .xlsx"
            type="file"
            @change="previewFiles"
          />
          <p>{{ fileData.name }}</p>
          
        </div>
      </div>

      <div class="row mb-3" v-if="errorMessageFlag">
        <div class="form-group col-md-6">
          <div class="alert bg-danger">
            <strong>{{ quantitySuccess }}</strong>
          </div>
        </div>
        <div class="col-xs-6 col-md-6 col-sm-6" style="margin-top: 10px">
          <button
            class="btn bg-danger"
            type="button"
            @click="downloadErrorImmportFile(fileNameError)"
          >
            <i class="fa fa-download"></i>
            {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
          </button>
        </div>
      </div>
      <div class="row" v-if="successMessageFlag">
        <div class="form-group col-md-12">
          <div class="alert bg-success">
            <strong>{{ $t("IMPORT.ANOTATION") }}</strong>
          </div>
        </div>
      </div>
    </div>
    <div v-if="type && listDataFile.length" class="row mb-12 evaluations">
      <div class="table-responsive">
        <table class="table table-bordered table-hover">
          <thead
            class="table-success"
            style="background-color: #24695c; color: #fff"
          >
            <tr>
              <th scope="col">#</th>
              <th scope="col">
                {{ $t("VISIT_PLAN.TABLE.LABEL.BRANCH_CODE") }}
              </th>
              <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.ZONAL") }}</th>
              <th scope="col">{{ $t("HOME.BTN.CHANNEL") }}</th>
              <th scope="col">{{ $t("DATE")+'(DD/MM/YYYY)' }}</th>
            </tr>
          </thead>
          <tbody v-for="(item, index) in listDataFile" :key="item">
            <tr>
              <td>{{index + 1}}</td>
              <td>{{item["Branch Code"]}}</td>
              <td>{{item.User}}</td>
              <td>{{item.Channel}}</td>
              <td>{{item.Date}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class="row mt-5">
    <div class="btn-showcase text-center">
      <button
        v-if="type == 1 && fileData"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importVisitPlan"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
      </button>
    </div>
    <div class="btn-showcase text-center">
      <button
        v-if="type == 2 && fileData"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importVisitPlan"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT_REMOVE") }}
      </button>
    </div>
  </div>
</template>
