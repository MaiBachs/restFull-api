<script setup>
import axios from "axios"
import moment from 'moment'
import { ref ,reactive, } from "vue"
import Multiselect from "@vueform/multiselect"
import VueDatePicker from "@vuepic/vue-datepicker"
import { useLoaddingStore } from './../../store/LoaddingStore'
import useBTS from "@/composables/useBTS"
import { getlistPolicy }  from './../../store/getlistPolicy'
import { getBranch } from "../../store/getBranch";
const getListBranch = getBranch();
const listfillPolicy = getlistPolicy();
const { exportSearchResult } = useBTS(ref)
console.log(listfillPolicy.listgetPolicy)
const loaddingStore = useLoaddingStore()
const listBranch = getBranch();
const searchData = reactive({
  isdn: "",
  br_code: "",
  bc_code: "",
  from_date: "",
  to_date: "",
  staff_code: "",
  current_page: 1,
});
const totalRecord = ref(0)
const fromDate = ref(new Date());
const toDate = ref(new Date());
const listResult = ref([]);
const checkListResult = ref(true);
function formatDate(date) {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}
function myCallback(newPage) {
  searchResult(newPage)
}
  // `const paginate = reactive ({
  //   total_page:"",
  //   current_page:"",
  //   total_record: ""
  // })`
const searchResult = async () => {
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD/MM/yyyy") : "";
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD/MM/yyyy") : "";
  listResult.value = [];
  getBTSDetail.value = [];
  checkListResult.value = true;
  try {
  loaddingStore.loading = true;
  const response = await axios.post('/api/plan-sale/search-result',{
  br_code: searchData.br_code,
  isdn: searchData.isdn,
  bc_code: searchData.bc_code,
  from_date: searchData.from_date,
  to_date: searchData.to_date,
  staff_code: searchData.staff_code,
  current_page: searchData.current_page,
  paging: true,
  });
    if (response && response.data.data.records && response.data.data.records.length) {
      const responseData = response.data.data;
      responseData.from_date = formatDate(new Date(responseData.from_date));
      responseData.to_date = formatDate(new Date(responseData.to_date));
      listResult.value  = response.data.data.records;
      // totalRecord.value = response.data.data.total_record;
      // paginate.total_page = responseData.total_page;
      // paginate.total_record = responseData.total_record;
      console.log(response.data.data.records);
      console.log(listResult.value);
    }else {
      checkListResult.value = false
    }
      loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
  }
}
const getBTSDetail = ref({
  branch_id: 0,
  bts_code: 0,
  from_date: 0,
  to_date: 0,
  sale_policy_id: 0,
  status: 0
});
function exportSearchResultBTS() {
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD/MM/yyyy") : "";
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD/MM/yyyy") : "";
  exportSearchResult({...searchData})
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
</script>
<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
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
          <label class="col-sm-4 col-form-label"
          >{{ $t("HOME.SEARCH.COMBOBOX.BC") }}:</label>
          <div class="col-sm-8">
            <Multiselect
                v-model="searchData.bc_code"
                :options="listBranch.getBranch || []"
                track-by="code"
                label="label"
                :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          >{{ $t("PHONE_NUMBER") }}:</label
          >
          <div class="col-sm-8">
            <input class="form-control" v-model="searchData.isdn" />
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          >{{ $t("USER") }}:</label>
          <div class="col-sm-8">
            <input class="form-control" v-model="searchData.staff_code" />
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("FROM_DATE") }}:</label>
          <div class="col-sm-8">
            <VueDatePicker
                v-model="fromDate"
                auto-apply
                format="dd-MM-yyyy"
            ></VueDatePicker>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("TO_DATE") }}:</label>
          <div class="col-sm-8">
            <VueDatePicker
                v-model="toDate"
                auto-apply
                format="dd-MM-yyyy"
            ></VueDatePicker>
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
            class="btn btn-pill btn-primary-gradien"
            type="button"
            @click="searchResult">
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
    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkListResult" class="table-responsive">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <fieldset v-if="listResult.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
            <tr>
              <th scope="col">#</th>
              <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BC") }}</th>
              <th scope="col">{{ $t("USER") }}</th>
              <th scope="col">{{ $t("PHONE_NUMBER") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE") }}</th>
              <th scope="col">{{ $t("CHANNEL") }}</th>
              <th scope="col">{{ $t("CONNECTION_TYPE") }}</th>
              <th scope="col">{{ $t("TYPE_OF_SERVICE") }}</th>
              <th scope="col">{{ $t("CALCULATED_DATE") }}</th>
              <th scope="col">{{ $t("RECHARGE") }}</th>
              <th scope="col">{{ $t("PRODUCT_CODE") }}</th>
            </tr>
            </thead>
            <tbody v-for="(item,index) in listResult" :key="item">
            <tr>
              <td>{{ (searchData.current_page-1)*10 +(index+1) }}</td>
              <td>{{ item.br_code }}</td>
              <td>{{ item.bc_code }}</td>
              <td>{{ item.owner_code }}</td>
              <td>{{ item.isdn }}</td>
              <td>{{ item.channel_code }}</td>
              <td>{{ item.bts_active_code }}</td>
              <td>{{ item.connect_type }}</td>
              <td>{{ item.service_type }}</td>
              <td>{{ item.caculate_date }}</td>
              <td>{{ item.recharge }}</td>
              <td>{{ item.product_code }}</td>
            </tr>
            </tbody>
          </table>
          <div class="pagination-search">
              <pagination v-model="searchData.current_page" :records="totalRecord" :per-page="10" @paginate="myCallback"/>
            </div>
        </div>
      </fieldset>
    </div>
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
