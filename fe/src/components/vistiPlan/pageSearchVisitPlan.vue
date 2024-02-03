<script setup>
import { reactive, ref, onMounted, watch } from "vue";
import { i18n } from "../../main.js";
import axios from "axios";
import moment from 'moment'
import { useLoaddingStore } from "../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
// import { getBranch } from "../../store/getBranch";
import useVisitPlan from "@/composables/useVisitPlan"
import { useUserStore } from './../../store/UserStore';
import deleteVisitPlan from '@/components/vistiPlan/deleteVisitPlan.vue'
import getUser from '@/composables/getUser'
import Modal from '@/components/common/customModal.vue' 
import ViewCheckList from '@/components/vistiPlan/viewCheckList.vue'


const  { getBC, getStaff, listBC, listStaff } = getUser(ref);
const userStore = useUserStore()  
const loaddingStore = useLoaddingStore();
// const getListBranch = getBranch();
const { exportSearchResult,getCheckList, listCateResult, checkListCateResult, checklistrequired } = useVisitPlan(ref)
const listSearchVisitPlan = ref([])
// const toDate = ref(new Date())
const totalRecord = ref(0)
const checkListSearch = ref(true)
const currentDateTimeDate = moment();
const currentDateTime = currentDateTimeDate.subtract(1, "days");
const checkAll = ref(false)
const showButtonRemoveAll = ref(false)
const planToDeleteIds = ref([])
const branch_id = ref(null)
const selectedBranch = ref("")
const showPopupCheckList = ref(false)
const plan = ref("")
const showCheckAll = ref(false)
const searchData = reactive({
  branch_id: "",
  bc_id: "",
  staff_id: "",
  is_visited: "",
  from_date: "",
  to_date: "",
  pos_code:"",
  current_page: 1,
});
const listIsVisited = [
  {
    label: "Si",
    value: 1,
  },
  {
    label: "No",
    value: 0,
  }
];
// const fromDate = ref(new Date(
//   toDate.value
//   .getFullYear(),
//   toDate.value.getMonth(),
//   1
// ));
const fromDate = ref();
const toDate = ref();
function myCallback(newPage) {
  searchVisitPlan(newPage)
}

function formatDate(value) {
  var getDate = ref("");
  if (value) {
    getDate.value = moment(value).format("DD-MM-YYYY");
    return getDate.value;
  } else {
    return getDate.value;
  }
}


async function searchVisitPlan() {
  checkListSearch.value = true
  listSearchVisitPlan.value = []
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD/MM/yyyy") : "";
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD/MM/yyyy") : "";
  try {
      loaddingStore.loading = true;
      const response = await axios.post('/api/visit-plan/search', searchData);
      if(response.data && response.data.data && response.data.data.visit_plan_beans.length ) {
          listSearchVisitPlan.value  = response.data.data.visit_plan_beans
          totalRecord.value = response.data.data.total_record;
          
          listSearchVisitPlan.value = listSearchVisitPlan.value.map(plan => {
            var planDateTime = new Date(plan.plan_date).getTime();
            console.log(planDateTime)
            console.log(currentDateTime)
            if(planDateTime >= currentDateTime) {
                plan.showIconAction = true;
                console.log(plan.showIconAction)
            }
            return plan;
        });

        
      }else {
        checkListSearch.value = false
      }
      loaddingStore.loading = false;
    } catch (error) {
      loaddingStore.loading = false;
      notify({
        type: "error",
        title: "Get current user",
        text: error,
        duration: 500,
      });
    }
}

function changeCheckPlan() {
  checkAll.value = true;
    showButtonRemoveAll.value = false;
    listSearchVisitPlan.value.forEach(function(visit) {
      console.log(visit)
        if(!visit.check && visit.showIconAction) {
            checkAll.value = false;
        }
        if(visit.check) {
            showButtonRemoveAll.value = true;
        }
    });
}

function changeCheckAll() {
  checkAll.value = true;
    listSearchVisitPlan.value.forEach(function(visit) {
        visit.check = checkAll.value;
    });
    checkAll.value ? showButtonRemoveAll.value = true : showButtonRemoveAll.value = false;
}
function decodeStatusChecklist(status) {
  var statusChecklist = null;
  if(status == 0 || status == null) {
      statusChecklist = i18n.global.t("NOT_YET");
  } else if (status == 1) {
      statusChecklist = i18n.global.t("MARKERTING.IMAGE.APPROVED");
  } else if (status == -1) {
      statusChecklist = i18n.global.t("REJECT");
  } else {
      statusChecklist = '';
  }

  return statusChecklist;
  
}

function channelToCheckListStatus(status) {
  var statusChecklist = null;
  if (status == null) {
      statusChecklist = i18n.global.t("UNDETERMINED");
  } else if(status == 0) {
      statusChecklist = i18n.global.t("UNQUALIFIED");
  } else if (status == 1) {
      statusChecklist = i18n.global.t("QUALIFIED");
  } else {
      statusChecklist = '';
  }
  

  return statusChecklist;
}
function exportSearchResultVisitPlan() {
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD/MM/yyyy") : "";
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD/MM/yyyy") : "";
  exportSearchResult({...searchData})
}
function clearDataBranch() {
  listBC.value = []
  listStaff.value = []
  searchData.bc_id = ""
  searchData.branch_id = ""
  searchData.staff_id = ""
}
async function selectBoxBranch() {
  await getBC(branch_id.value)
}
async function selectBoxBc() {
  getStaff(searchData.bc_id)
}
async function selectBoxUserType() {
  listStaff.value = []
  try {
    loaddingStore.loading = true;
    const response = await axios.post('/api/staff/search', {
        shop_id: searchData.bc_id || '',
        user_type: searchData.userType || ''
    });
    console.log(response)
    if(response.data && response.data.data && response.data.data.length) {
      listStaff.value = response.data.data.map(function(o) {
        return {
          value: o.id,
          label: o.code + '-' + o.name
        }
      })
    }
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 500,
    });
  }
}


async function showChecklist(planView) {
  plan.value = planView
  await getCheckList(planView.id)
  showPopupCheckList.value = true

}

function deleteVisitPlan2Confirm (plan) {
  planToDeleteIds.value = [plan.id];

}
function clearListSearchVisitPlan() {
  planToDeleteIds.value = []
  showPopupCheckList.value = false;
  searchVisitPlan()
}


function removeAllVisitPlans() {
   planToDeleteIds.value = [];
   listSearchVisitPlan.value.forEach(function(visit) {
        if(visit.check) {
          planToDeleteIds.value.push(visit.id)
        }
    });
    console.log(planToDeleteIds.value)
}
onMounted(async () => {
  if (userStore.currentUser.staff_code !== 'Loadding') {
    await userStore.fetchDataGetBranch();
  }
});

watch(() => userStore.listBranch, (newVal) => {
  if (newVal && newVal.length) {
    searchData.branch_id = newVal[0].value;
  }
});




</script>
<template>
  <div class="card-body p-0">
    <div class="row m-b-15">
      <div class="col align-self-start">
        <div class="m-b-5 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="branch_id"
              :options="userStore.listBranch || []"
              track-by="name"
              @select="selectBoxBranch"
              @clear="clearDataBranch"
              :canClear="false"
              :value="selectedBranch"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="m-b-5 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.COMBOBOX.BC") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.bc_id"
              :options="listBC || []"
              track-by="name"
              @select="selectBoxBc"
              @clear="clearData"
              :canClear="false"
              
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="m-b-5 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.COMBOBOX.TYPEUSER") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.pos_code"
              :options="userStore.userType || []"
              track-by="name"
              @select="selectBoxUserType"
              :placeholder="i18n.global.t('INPUT.VALUE')"
            ></Multiselect>
          </div>
        </div>
      </div>
    </div>

    <div class="row m-b-15">
      <div class="col align-self-start">
        <div class="m-b-5 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.COMBOBOX.ZONAL") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.staff_id"
              :options="listStaff || []"
              track-by="name"
              @select="selectBoxStaff"
              :canClear="false"
              :placeholder="i18n.global.t('INPUT.VALUE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="m-b-5 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("VISIT_PLAN.TABLE.LABEL.VISITED") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.is_visited"
              :options="listIsVisited || []"
              track-by="name"
              @select="selectBoxIsVisited"
              :placeholder="i18n.global.t('INPUT.VALUE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start"></div>
    </div>

    <div class="row m-b-15">
      <div class="col align-self-start">
        <div class="m-b-5 row">
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
        <div class="m-b-5 row">
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
      <div class="col align-self-start"></div>
    </div>
    <div class="row p-15">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary"
          type="button"
          @click="searchVisitPlan"
        >
          {{ $t("SEARCH") }}
        </button>
        <button
          class="btn btn-pill btn-add"
          type="button"
          @click="exportSearchResultVisitPlan"
        >
        {{ $t("EXPORT_DETAIL") }}
        </button>
        <button
          v-if="showButtonRemoveAll"
          class="btn btn-pill btn-danger"
          type="button"
          data-bs-toggle="modal"
          data-original-title="test"
          data-bs-target="#openWarningModal"
          @click="removeAllVisitPlans"
        >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.REMOVE_ALL") }}
        </button>
        
      </div>
    </div>
    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkListSearch" class="table-responsive">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <fieldset v-if="listSearchVisitPlan.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
              <tr>
                <th scope="col">#</th>
                <th scope="col">
                  <label v-if="showCheckAll" style="margin-bottom: 0px ;">
                    <input @click="changeCheckAll" style="width: 15px; height: 15px;" type="checkbox" v-model="checkAll">
                  </label>
                </th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.VISITED') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.PLANVISIT') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.BRANCH_CODE') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.SHOP_CODE') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.ZONAL_CODE') }}</th>
                <th scope="col">{{ $t('HOME.SEARCH.COMBOBOX.TYPEUSER') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.CHANCEL') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.PLANDATE') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.CHECKEDIN') }}</th>
                <th scope="col">{{ $t('REVIEW') }}</th>
                <th scope="col">{{ $t('CHECKLIST_STATUS') }}</th>
                <th scope="col">{{ $t('VISIT_PLAN.TABLE.LABEL.ACTION') }}</th>
                <th scope="col">{{ $t('CHECK_LIST') }}</th>
              </tr>
              </thead>
              <tbody v-for="(plan ,index) in listSearchVisitPlan" :key="plan">
                <tr>
                  <td>{{(searchData.current_page-1)*10 +(index+1)}}</td>
                  <td >
                      <label v-if="plan.showIconAction" style="margin-bottom: 0px ;">
                        <input @click="changeCheckPlan" type="checkbox" v-model="plan.check">
                      </label>
                    </td>
                    <td v-if="plan.checked_in" style="display: flex; justify-content: center;">
                        <i style="font-size: 20px; padding: 10px;" class="fa fa-check-circle-o icon-label-search"></i>
                    </td>
                    <td v-if="!plan.checked_in">{{plan.checked_in}}
                    </td>
                    <td>{{formatDate(plan.plan_date)}}</td>
                    <td>{{plan.branch_code}}</td>
                    <td>{{plan.bc_code}}</td>
                    <td>{{plan.user_code}}</td>
                    <td>{{plan.pos_code}}</td>
                    <td>{{plan.channel_code}}</td>
                    <td>{{formatDate(plan.created_date)}}</td>
                    <td>{{formatDate(plan.checked_in)}}</td>
                    
                    <td>
                        <p v-if="plan.check_list_result_status == 1 || plan.check_list_result_status == 0 || plan.check_list_result_status == null">{{ decodeStatusChecklist(plan.check_list_result_status) }}</p>
                        <button v-if="plan.check_list_result_status == -1"  @click="showComment(plan.check_list_result_status)" style="background: transparent;border: none; text-decoration-line: underline; padding: 0px; font-weight: 500;">{{decodeStatusChecklist(plan.check_list_result_status)}}</button>
                    </td>
                    <td>
                        {{ channelToCheckListStatus(plan.channel_to_check_list_status) }}
                    </td>
                    <td>
                        <div class="text-center" v-if="plan.showIconAction && !plan.checked_in">
                          <button type="button" 
                          data-bs-toggle="modal"
                          data-original-title="test"
                          data-bs-target="#openWarningModal"
                          class="btn btn-danger btn-icon btn-icon-smt btn-xs" 
                          @click="deleteVisitPlan2Confirm(plan)"><i class="icofont icofont-ui-delete"></i></button>
                        </div>
                    </td>
                    <td>
                        <button v-if="plan.checked_in && plan.is_detail != null && plan.is_detail == 1" @click="showChecklist(plan)" style=" border: none; background: transparent;color: darkcyan; text-decoration-line: underline;">{{ $t('VIEW_CHECKLIST') }}</button>
                    </td>

                    
                  
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
  <deleteVisitPlan @clearValue="clearListSearchVisitPlan" :planToDeleteIds="planToDeleteIds"/>
  <Modal v-model="showPopupCheckList" :title="$t('CHECK_LIST')">
    <ViewCheckList :listCateResult = "listCateResult" :checkListCateResult="checkListCateResult" :checklistrequired="checklistrequired" :plan="plan" @clearValue="clearListSearchVisitPlan"/>
  </Modal>
</template>

<style scoped>
th {
  font-size: 12px;
  white-space:nowrap
}
td {
  font-size: 13px;
}
</style>
