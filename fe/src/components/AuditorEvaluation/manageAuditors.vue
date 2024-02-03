<script setup>
import axios from "axios"
import moment from 'moment'
import {ref, reactive} from "vue"
import Multiselect from "@vueform/multiselect"
import VueDatePicker from "@vuepic/vue-datepicker"
import { getBranch } from './../../store/getBranch'
import { getChannelType } from './../../store/getChannelType'
import { getShopChannelAudit } from '../../store/getShopChannel'
import { useLoaddingStore } from './../../store/LoaddingStore'
import { useAuditEvalutionStore } from '../../store/AuditEvalutionStore'
import Pagination from 'v-pagination-3';
const loaddingStore = useLoaddingStore()

const listBranch = getBranch();
const listChannelType = getChannelType();
const auditEvaluation = useAuditEvalutionStore();
const shopChannelAudit = getShopChannelAudit();
// const fromDate = ref(new Date());
// fromDate.value = moment(new Date()).subtract(1, 'months');
// const toDate = ref(new Date());
const fromDate = ref();
const toDate = ref();
const auditorCheckLists = ref([])
const checkShowData = ref(true)
const searchData = reactive({
  branch_id: "",
  channel_type_id: "",
  auditor_id: "",
  shop_channel_id: "",
  from_date: "",
  to_date: "",
  current_page: 1
});
const paginate = reactive ({
  totalPage:"",
  currentPage:"",
  totalRecord: ""
})
const searchAuditEvaluation = async () => {
  loaddingStore.loading = true;
  checkShowData.value = true;
  auditorCheckLists.value = []
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD-MM-yyyy") : "";
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD-MM-yyyy") : "";

  const response = await axios.post(
    "/api/auditor-config/search",
    searchData
  );
  if(response && response.data.auditor_check_lists
    && response.data.auditor_check_lists.length) {
      checkShowData.value = true
      auditorCheckLists.value = response.data.auditor_check_lists
      paginate.totalPage = response.data.total_page;
      paginate.totalRecord = response.data.total_record;
  }else{
    checkShowData.value = false
    auditorCheckLists.value = []
  }
    loaddingStore.loading = false;

}

const getAuditorConfigDetail = ref([]);
const checkDataDetail = ref(false)

const viewDetailAudit = async (auditorConfigId) => {
  getAuditorConfigDetail.value = []
  loaddingStore.loading = true;
  const response  = await axios.get(`/api/auditor-config/id=${auditorConfigId}`);
  loaddingStore.loading = false;
  if (response && response.data) {
    getAuditorConfigDetail.value = [response.data];
    console.log(getAuditorConfigDetail.value)
  } else {
    checkDataDetail.value = true
  }
};

function myCallback(newPage) {
  searchAuditEvaluation(newPage)
}
const exportSearchResult = async () => {
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD/MM/yyyy") : "";
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD/MM/yyyy") : "";
  auditEvaluation.export(searchData)
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
              v-model="searchData.branch_id"
              :options="listBranch.getBranch || []"
              track-by="name"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("AUDITTORS") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.auditor_id"
              :options="auditEvaluation.listAuditor|| []"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>

      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.channel_type_id"
              :options="listChannelType.listChannelType || []"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      
    </div>

    <div class="row mb-3">
      
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

      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.MAP.SHOP_CHANNEL") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.shop_channel_id"
              :options="shopChannelAudit.listShopChannel || []"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
    </div>

    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchAuditEvaluation()"
        >
        {{ $t("SEARCH") }}
        </button>
        <button
          class="btn btn-pill"
          style="background-color: #4682b4; color: #fff"
          type="button"
          @click="exportSearchResult"
        >
        {{ $t("EXPORT_DETAIL") }}
        </button>
      </div>
    </div>

    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkShowData" class="table-responsive">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <fieldset v-if="auditorCheckLists.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
              <tr>
                <th scope="col">#</th>
                  <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
                  <th scope="col">{{ $t("AUDITTORS") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.SHOP_CHANNEL") }}</th>
                  <th scope="col">{{ $t("CREATE_DATE") }}</th>
                  <th scope="col">{{ $t("CREATED_USER") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
                </tr>
              </thead>
              <tbody v-for="(item ,index) in auditorCheckLists" :key="item">
                <tr>
                  <td>{{index + 1  + (searchData.current_page - 1)*10}}</td>
                  <td>{{ item.br_code }}</td>
                  <td>{{ item.auditor }}</td>
                  <td>{{ item.channel_type_name }}</td>
                  <td>{{ item.shop_channel }}</td>
                  <td>{{ new Date(item.created_date).toLocaleDateString('en-GB') }}</td>
                  <td>{{ item.created_by }}</td>
                  <td>
                    <div class="buttons">
                      <button
                      class="btn btn-primary btn-xs"
                      @click="viewDetailAudit(item.id)"
                    >
                    <i class="fa fa-edit"></i>
                    </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="pagination-search">
              <Pagination v-model="searchData.current_page" :records="paginate.totalRecord" :per-page="10" @paginate="myCallback"/>
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
.pagination-search {
  float: right;
}
</style>
../../store/getChannelTypeAuditor