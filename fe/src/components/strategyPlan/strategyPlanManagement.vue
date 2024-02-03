<script setup>
import axios from "axios";
import moment from "moment";
import { ref, reactive } from "vue";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import useBTS from "@/composables/useBTS";
import { getlistPolicy } from "./../../store/getlistPolicy";
// import { notify } from "@kyvg/vue3-notification";
import { getBranch } from "../../store/getBranch";
import * as XLSX from "xlsx";
import { useItemConfigStore } from "./../../store/ItemConfigStore";
// const pageImport = ref(1);
const listDataFileShow = ref([]);
const itemConfigStore = useItemConfigStore();
const listDataFile = ref("");
const loaddingStore = useLoaddingStore();
const type = ref("");
const quantitySuccess = ref("");
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref("");
const inputFile = ref(null);
const fileData = ref("");
const getListBranch = getBranch();
const itemEdit = ref({});
const originItem = ref({});
const listfillPolicy = getlistPolicy();
const { exportSearchResult } = useBTS(ref);
console.log(listfillPolicy.listgetPolicy);
const listBranch = getBranch();
const year = ref(new Date().getFullYear());
const searchData = reactive({
  sale_policy_id: "",
  br_code: "",
  plan_date: "",
  target_level: 1,
  current_page: 1,
});
const totalRecord = ref(0);
const fromDate = ref(new Date());
const toDate = ref(new Date());
const listSale = ref([]);
const checkListSale = ref(true);
function myCallback(newPage) {
  searchBTS(newPage);
}
const paginate = reactive({
  total_page: "",
  current_page: "",
  total_record: "",
});
const searchBTS = async () => {
  searchData.plan_date = year;
  listSale.value = [];
  getBTSDetail.value = [];
  checkListSale.value = true;
  try {
    loaddingStore.loading = true;
    const response = await axios.post("/api/plan-sale/search", {
      br_code: searchData.br_code,
      plan_date: searchData.plan_date,
      target_level: 1,
      current_page: searchData.current_page,
      paging: true,
    });
    if (response && response.data && response.data.length) {
      listSale.value = response.data;
      totalRecord.value = response.data.data.total_record;
      paginate.total_page = response.data.total_page;
      paginate.total_record = response.data.total_record;
      console.log(listSale);
    } else {
      checkListSale.value = false;
    }
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    // notify({
    //   type: "error",
    //   title: "Get current user",
    //   text: error,
    //   duration: 500,
    // });
  }
};
const getBTSDetail = ref({
  branch_id: 0,
  bts_code: 0,
  start_date: 0,
  end_date: 0,
  sale_policy_id: 0,
  status: 0,
});
const showSaveButton = ref(false);
const checkDataDetail = ref(false);
const getDetailEdit = async (item, data) => {
  itemEdit.value = [];
  itemEdit.value = data;
  originItem.value = { ...data };
  showSaveButton.value = false;
  if (item) {
    getBTSDetail.value = JSON.parse(JSON.stringify(item));
  } else {
    checkDataDetail.value = true;
  }
  console.log(item);
};
// const dateTime = (date) => {
//   var getDate = ref("");
//   if (date) {
//     getDate.value = moment(date).format("DD-MM-YYYY");
//     return getDate.value;
//   } else {
//     return getDate.value;
//   }
// };
function clearListBTS() {
  searchBTS();
}
function exportSearchResultBTS() {
  searchData.from_date = fromDate.value
    ? moment(fromDate.value).format("DD/MM/yyyy")
    : "";
  searchData.to_date = toDate.value
    ? moment(toDate.value).format("DD/MM/yyyy")
    : "";
  exportSearchResult({ ...searchData });
}
function selectBoxBranch(selectedValue) {
  console.log(selectedValue);
  searchData.br_code = "";
  const selectedOption = getListBranch.getBranch.find(
    (option) => option.value === selectedValue
  );
  if (selectedOption) {
    searchData.br_code = selectedOption.code;
  }
  console.log(searchData.br_code);
}
const previewFiles = async () => {
  listDataFile.value = [];
  listDataFileShow.value = [];
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  fileData.value = inputFile.value.files[0];
  const file = fileData.value;
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
      listDataFileShow.value = XLSX.utils.sheet_to_json(
        wb.Sheets[wb.SheetNames[0]],
        {
          header: 0,
          raw: false,
          dateNF: "DD/MM/YYYY",
        }
      );
      const dataWithStatus = listDataFileShow.value.map((item) => ({
        ...item,
        status: true,
        errorMesage: "",
      }));
      listDataFile.value = dataWithStatus;
      console.log(listDataFile.value);
      if (listDataFile.value && listDataFile.value.length) {
        totalRecord.value = listDataFile.value.length;
        myCallback();
      }
    };
    console.log(listDataFile.value);
    reader.readAsBinaryString(file);
  }
  inputFile.value.value = null;
  quantitySuccess.value = null;
  fileNameError.value = null;
};
const planDate = ref(""); 
const importTarget = async () => {
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
    const response = await axios.post('/api/plan-sale/import?targetLevel=1&planDate=' + planDate.value, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    console.log(response);
    if (response.data.fileImportResultName) {
      errorMessageFlag.value = true;
      quantitySuccess.value = response.data.errorMessageImport;
      fileNameError.value = response.data.fileImportResultName;
    } else {
      listDataFile.value = [];
      successMessageFlag.value = true;
    }
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    console.log(error);
    // notify({
    //   type: "error",
    //   title: "Get current user",
    //   text: error,
    //   duration: 500,
    // });
  }
};
const downloadFileTemplate = async () => {
  itemConfigStore.downloadFile(`Template_import_BTS.xlsx`);
};
const downloadErrorImmportFile = () => {
  itemConfigStore.downloadFileError(fileNameError.value);
};
</script>

<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-2 col-form-label"
            >{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="br_code"
              :options="listBranch.getBranch || []"
              track-by="code"
              label="label"
              @input="selectBoxBranch"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <button class="btn btn-light" style="width: 50%; color: #24695c">
            <i class="fa fa-download"></i>
            <a @click="downloadFileTemplate">
              {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE") }}
            </a>
          </button>
        </div>
      </div>
    </div>

    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-2 col-form-label">{{ $t("DATE") }}:</label>
          <div class="col-sm-8">
            <VueDatePicker
              v-model="planDate"
              auto-apply
              year-picker
              format="yyyy"
            ></VueDatePicker>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
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

    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchBTS"
        >
          {{ $t("SEARCH") }}
        </button>

        <button
          class="btn btn-pill btn-add"
          type="button"
          @click="exportSearchResultBTS"
        >
          {{ $t("EXPORT_DETAIL") }}
        </button>
      </div>
    </div>
    <div v-if="listDataFile.length" class="row mb-3 evaluations">
      <div class="table-responsive">
        <table class="table table-bordered table-hover">
          <thead
            class="table-success"
            style="background-color: #24695c; color: #fff"
          >
            <tr>
              <th scope="col">#</th>
              <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
              <th scope="col">Enero</th>
              <th scope="col">Febrero</th>
              <th scope="col">Marzo</th>
              <th scope="col">Abril</th>
              <th scope="col">Mayo</th>
              <th scope="col">Junio</th>
              <th scope="col">Julio</th>
              <th scope="col">Agosto</th>
              <th scope="col">Septiembre</th>
              <th scope="col">Octubre</th>
              <th scope="col">Noviembre</th>
              <th scope="col">Diciembre</th>
              <th scope="col">Total</th>
            </tr>
          </thead>
          <tbody v-for="(item, index) in listDataFile" :key="item">
            <tr>
              <td>{{ index + 1 }}</td>
              <td>{{ item["Código de sucursal"] }}</td>
              <td>{{ item["Enero"] }}</td>
              <td>{{ item["Febrero"] }}</td>
              <td>{{ item["Marzo"] }}</td>
              <td>{{ item["Abril"] }}</td>
              <td>{{ item["Mayo"] }}</td>
              <td>{{ item["Junio"] }}</td>
              <td>{{ item["Julio"] }}</td>
              <td>{{ item["Agosto"] }}</td>
              <td>{{ item["Septiembre"] }}</td>
              <td>{{ item["Octubre"] }}</td>
              <td>{{ item["Noviembre"] }}</td>
              <td>{{ item["Diciembre"] }}</td>
              <td>{{ item["Total"] }}</td>
            </tr>
          </tbody>
        </table>
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
    <div class="row">
      <div class="btn-showcase text-center">
        <button
          v-if="listDataFile.length"
          class="btn btn-pill btn-primary-gradien"
          type="button"
          style="margin-top: 10px"
          @click="importTarget()"
        >
          {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
        </button>
      </div>
    </div>
    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkListSale" class="table-responsive">
        <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
      </fieldset>
      <fieldset v-if="listSale.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead
              class="table-success"
              style="background-color: #24695c; color: #fff"
            >
              <tr>
                <th scope="col" rowspan="2">#</th>
                <th scope="col" rowspan="2">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
                <th scope="col" colspan="3">January</th>
                <th scope="col" colspan="3">February</th>
                <th scope="col" colspan="3">March</th>
                <th scope="col" colspan="3">April</th>
                <th scope="col" colspan="3">May</th>
                <th scope="col" colspan="3">June</th>
                <th scope="col" colspan="3">July</th>
                <th scope="col" colspan="3">August</th>
                <th scope="col" colspan="3">September</th>
                <th scope="col" colspan="3">October</th>
                <th scope="col" colspan="3">November</th>
                <th scope="col" colspan="3">December</th>
                <th scope="col" colspan="3">Total</th>
                <th scope="col" rowspan="2">Action</th>
              </tr>
              <tr>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
                <td class="text-center text-target">T</td>
                <td class="text-center text-danger">P</td>
                <td class="text-center text-result">R</td>
              </tr>
            </thead>
            <tbody v-for="(item, index) in listSale" :key="item">
              <tr>
                <td>{{ (searchData.current_page - 1) * 10 + (index + 1) }}</td>
                <td>{{ item.br_code }}</td>
                <th scope="col"  class="text-center text-target">{{ item.t1_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t1_plan === null ? "0" : item.t1_plan }}</th>
                <th scope="col" class="text-center text-result">{{ item.t1_result === null ? "0" : item.t1_result }}</th>
                <th scope="col" class="text-center text-target">{{ item.t2_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t2_plan === null ? "0" : item.t2_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t2_result === null ? "0" : item.t2_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t3_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t3_plan === null ? "0" : item.t3_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t3_result === null ? "0" : item.t3_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t4_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t4_plan === null ? "0" : item.t4_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t4_result === null ? "0" : item.t4_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t5_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t5_plan === null ? "0" : item.t5_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t5_result  === null ? "0" : item.t5_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t6_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t6_plan === null ? "0" : item.t6_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t6_result === null ? "0" : item.t6_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t7_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t7_plan === null ? "0" : item.t7_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t7_result === null ? "0" : item.t7_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t8_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t8_plan === null ? "0" : item.t8_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t8_result === null ? "0" : item.t8_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t9_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t9_plan === null ? "0" : item.t9_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t9_result === null ? "0" : item.t9_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t10_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t10_plan === null ? "0" : item.t10_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t10_result === null ? "0" : item.t10_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t11_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t11_plan === null ? "0" : item.t11_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t11_result === null ? "0" : item.t11_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.t12_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.t12_plan === null ? "0" : item.t12_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.t12_result === null ? "0" : item.t12_result}}</th>
                <th scope="col" class="text-center text-target">{{ item.total_target }}</th>
                <th scope="col" class="text-center text-danger">{{ item.total_plan === null ? "0" : item.total_plan}}</th>
                <th scope="col" class="text-center text-result">{{ item.total_result === null ? "0" : item.total_result}}</th>
                <td>
                  <div class="buttons">
                    <button
                      class="btn btn-primary btn-xs"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleModalEditBTS"
                      @click="getDetailEdit(item, data)"
                    >
                      <i class="fa fa-edit"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="pagination-search">
            <pagination
              v-model="searchData.current_page"
              :records="totalRecord"
              :per-page="10"
              @paginate="myCallback"
            />
          </div>
        </div>
      </fieldset>
    </div>
    <btsEdit
      @clearValue="clearListBTS"
      :itemAdd="getBTSDetail"
      v-model="getBTSDetail"
      :item="item"
      :options="options"
      :listPolicy="listPolicy"
      :ListSaleTime="ListSaleTime"
      :start_date="getBTSDetail.start_date"
      :end_date="getBTSDetail.end_date"
    />
    <confirmEditActive
      @clearValue="clearListBTS"
      :itemAdd="getBTSDetail"
      v-model="getBTSDetail"
      :item="item"
    />
  </div>
</template>

<style lang="scss" scoped>
.multiselect {
  max-height: 35px;
  min-height: 30px;
}
.dp__input_wrap {
  height: 35px;
  display: flex;
}
.evaluations {
  fieldset {
    padding: 15px;
    margin: 0 2px;
    border: 1px solid #c0c0c0;
  }
  legend {
    float: initial;
    font-size: 14px;
    width: auto;
    border-bottom: none;
    padding: 0px 5px;
  }
  .no-item {
    font-weight: 500;
  }
  svg {
    width: 16px;
    height: 16px;
  }
}
</style>
