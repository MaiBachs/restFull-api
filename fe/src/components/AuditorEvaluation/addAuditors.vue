<script setup>
import { ref, watch } from "vue";
import Multiselect from "@vueform/multiselect";
import { useLoaddingStore } from "../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import { useItemConfigStore } from "./../../store/ItemConfigStore";
import { useEvalutionStore } from "./../../store/EvalutionStore";
import axios from "axios";
import * as XLSX from "xlsx";
import { i18n } from '../../main.js';
const loaddingStore = useLoaddingStore();
const itemConfigStore = useItemConfigStore();
const evaluationConfigStore = useEvalutionStore();
const type = ref("");
const quantitySuccess = ref("");
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref("");
const listDataFile = ref("");
const changeType = () => {
  listDataFile.value = [];
};
const options = [
  {
    title: i18n.global.t("VISIT_PLAN.IMPORT.LABEL.IMPORT"),
    value: "1",
  },
  {
    title: i18n.global.t("VISIT_PLAN.IMPORT.LABEL.IMPORT_REMOVE"),
    value: "2",
  },
];
const inputFile = ref(null);
const fileData = ref("");
const previewFiles = async () => {
  listDataFile.value = [];
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  fileData.value = inputFile.value.files[0];

  const file = fileData.value; // Lấy tệp đầu tiên nếu có nhiều tệp
  if(file) {
    var reader = new FileReader();
    reader.onload = () => {
      var fileData = reader.result;
      var wb = XLSX.read(fileData, {
        type: "binary",
        cellText: false,
        cellDates: true,
      });
      listDataFile.value = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]], {
        header: 0,
        raw: false,
        dateNF: "DD/MM/YYYY",
      });
    };
    reader.readAsBinaryString(file);
  }
  
  inputFile.value.value = null
};

const importEvaluations = async () => {
  listDataFile.value = [];
  const formData = new FormData();
  formData.append("file", fileData.value);
  if (type.value == 1) {
    formData.append("action", "ADD");
  } else if (type.value == 2) {
    formData.append("action", "DELETE");
  }

  try {
    loaddingStore.loading = true;
    const response = await axios.post("/api/auditor-config/import", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    if (response.data.data == null) {
      quantitySuccess.value = response.data.status.message;
      fileNameError.value = response.data.status.code;
    }else{
      quantitySuccess.value = response.data.status.message;
    }
    if (fileNameError.value) {
      errorMessageFlag.value = true;
    } else {
      successMessageFlag.value = true;
    }
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    successMessageFlag.value = false; 
    listDataFile.value = [];
    errorMessageFlag.value = false;
    console.log(error);
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 500,
    });
  }
};

const downloadErrorImmportFile = () => {
  itemConfigStore.downloadFileError(fileNameError.value);
};

const downloadFileTemplate = async () => {
  evaluationConfigStore.downloadFileTemplate("template_import_auditor.xls",0);
};

watch(type, (newValue) => {
  if (!newValue) {
    successMessageFlag.value = false;
    listDataFile.value = [];
    errorMessageFlag.value = false;
  }
});


</script>
<template>
  <div class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label"
          >{{ $t("TYPE_OF_ACTION") }}:</label
        >
        <div class="col-sm-6">
          <multiselect
            v-model="type"
            deselect-label="Can't remove this value"
            track-by="title"
            v-on:change="changeType()"
            label="title"
            :placeholder="$t('SELECT_ONE')"
            :options="options"
            :searchable="false"
            :allow-empty="false"
          >
          </multiselect>
        </div>
      </div>
    </div>
    <div class="col-md-4 text-center">
      <div class="mb-3 row">
        <button class="btn btn-light" style="width: 100%; color: #24695c">
          <i class="fa fa-download"></i>
          <a @click="downloadFileTemplate">
            {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE") }}
          </a>
        </button>
      </div>
    </div>
  </div>
  <div v-if="type" class="rowmb-3">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label text-left"
          >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
        >
        <div class="col-sm-8 col-form-label">
          <input
            id="file"
            ref="inputFile"
            accept=".xls, .xlsx"
            type="file"
            @change="previewFiles"
          />
        </div>
      </div>
    </div>
  </div>

  <div v-if="type && listDataFile.length" class="row mb-3 evaluations">
    <div class="table-responsive">
      <table class="table table-bordered table-hover">
        <thead
          class="table-success"
          style="background-color: #24695c; color: #fff"
        >
          <tr>
            <th scope="col">#</th>
            <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
            <th scope="col">{{ $t("AUDITTORS") }}</th>
            <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
            <th scope="col">{{ $t("CHANNEL_CODE") }}</th>
          </tr>
        </thead>
        <tbody v-for="(item, index) in listDataFile.slice(1)" :key="item">
          <tr>
            <td>{{ index + 1 }}</td>
            <td>{{ item.__EMPTY }}</td>
            <td>{{ item.__EMPTY_1 }}</td>
            <td>{{ item.__EMPTY_2 }}</td>
            <td>{{ item.__EMPTY_3 }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <div class="row mb-3" v-if="errorMessageFlag && type == 2">
    <div class="form-group col-md-6">
      <div class="alert bg-danger">
        <strong>{{ $t("REMOVE.ANOTATION") }}{{ quantitySuccess.slice(14,19) }}{{ $t("RECORDS") }} </strong>
      </div>
    </div>
    <div class="col-xs-6 col-md-6 col-sm-6" style="margin-top: 10px">
      <button
        class="btn bg-danger"
        type="button"
        @click="downloadErrorImmportFile"
      >
        <i class="fa fa-download"></i>
        {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
      </button>
    </div>
  </div>
  <div class="row mb-3" v-if="errorMessageFlag && type == 1">
    <div class="form-group col-md-6">
      <div class="alert bg-danger">
        <strong>{{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}{{ quantitySuccess.slice(14,19) }}{{ $t("RECORDS") }} </strong>
      </div>
    </div>
    <div class="col-xs-6 col-md-6 col-sm-6" style="margin-top: 10px">
      <button
        class="btn bg-danger"
        type="button"
        @click="downloadErrorImmportFile"
      >
        <i class="fa fa-download"></i>
        {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
      </button>
    </div>
  </div>
  <div class="row" v-if="successMessageFlag">
    <div class="form-group col-md-12">
      <div class="alert bg-success">
        <strong>{{ quantitySuccess }}</strong>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="btn-showcase text-center">
      <button
        v-if="type == 1"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importEvaluations()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
      </button>
      <button
        v-if="type == 2"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importEvaluations()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT_REMOVE") }}
      </button>
    </div>
  </div>
</template>
