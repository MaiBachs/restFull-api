<script setup>
import axios from "axios";
import moment from "moment";
import { i18n } from "../../main.js";
import { ref, reactive, computed } from "vue";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import useEvaluation from "@/composables/useEvaluation";
import { notify } from "@kyvg/vue3-notification";
import { getBranch } from "./../../store/getBranch";
import { getChannelType } from "./../../store/getChannelType";
import { getJobsToPlan } from "./../../store/getJodChannel";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import { useEvalutionStore } from "./../../store/EvalutionStore";
import popupModal from "../../app/popupModal.vue";
import popupModalError from "@/components/common/popupModalError.vue";
import errorEditEvaluation from "@/components/evaluation/errorEditEvaluation.vue";
import EvaluationItemEdit from "../../components/evaluation/EvaluationItemEdit.vue";
import Pagination from "v-pagination-3";
// import { scrollTo } from 'vue-scrollto';

const loaddingStore = useLoaddingStore();
const evalutionStore = useEvalutionStore();
const { addEvaluationQueue, evaluationQueue } = useEvaluation(ref);

const listBranch = getBranch();
const listChannelType = getChannelType();
const listJod = getJobsToPlan();
const listEvaluations = ref([]);
const getEvaluationsDetail = ref([]);
const branchEdit = ref("");
const checkListEvalution = ref(true);
const itemEdit = ref({});
const originItem = ref({});
const showSaveButton = ref(false);
const listItemEditId = ref([]);
const checkDataDetail = ref(false);
const getjobCodeEvalution = ref("");
const searchData = reactive({
  branch_id: "",
  channel_type_id: "",
  job_id: "",
  from_date: "",
  to_date: "",
  current_page: 1,
});
const paginate = reactive({
  totalPage: "",
  currentPage: "",
  totalRecord: "",
});
const fromDate = ref();
const toDate = ref();
// const fromDate = ref(new Date());
// fromDate.value = moment(new Date()).subtract(1, "months");
// const toDate = ref(new Date());
const canOk = computed(
  () => JSON.stringify(itemEdit.value) !== JSON.stringify(originItem.value)
);

const searchEvaluation = async () => {
  evalutionStore.dataSearch = [];
  showSaveButton.value = false;
  listItemEditId.value = [];
  getEvaluationsDetail.value = [];
  listEvaluations.value = [];
  checkListEvalution.value = true;
  searchData.from_date = fromDate.value
    ? moment(fromDate.value).format("DD-MM-yyyy")
    : "";
  searchData.to_date = toDate.value
    ? moment(toDate.value).format("DD-MM-yyyy")
    : "";
  // if(fromDate.value > toDate.value) {
  //   notify({
  //       type: "error",
  //       title: i18n.global.t('HOME.SEARCH.ERROR.FROMDATE_BEFORE_TODATE'),
  //       duration: 10000,
  //     });
  //     return
  if (toDate.value && fromDate.value > toDate.value) {
    notify({
      type: "error",
      title: i18n.global.t("HOME.SEARCH.ERROR.FROMDATE_BEFORE_TODATE"),
      duration: 2000,
    });
    return;
  } else {
    loaddingStore.loading = true;
    const response = await axios.post(
      "/api/evaluation-config/search",
      searchData
    );
    if (
      response &&
      response.data.evaluations &&
      response.data.evaluations.length
    ) {
      checkListEvalution.value = true;
      listEvaluations.value = response.data.evaluations;
      searchData.current_page = response.data.current_page + 1;
      paginate.totalPage = response.data.total_page;
      paginate.totalRecord = response.data.total_record;
    } else {
      checkListEvalution.value = false;
      listEvaluations.value = [];
    }
    loaddingStore.loading = false;
  }
};

const exportEvaluation = async () => {
  searchData.from_date = fromDate.value
    ? moment(fromDate.value).format("DD-MM-yyyy")
    : "";
  searchData.to_date = toDate.value
    ? moment(toDate.value).format("DD-MM-yyyy")
    : "";
  evalutionStore.export(searchData);
};

const getDetail = async (evaluationId, jobCode, branch_code) => {
  getjobCodeEvalution.value = jobCode;
  branchEdit.value = branch_code;
  getEvaluationsDetail.value = [];
  evaluationQueue.value = [];
  showSaveButton.value = false;
  loaddingStore.loading = true;

  const response = await axios.get(
    `/api/evaluation-config/details/id=${evaluationId}`
  );

  loaddingStore.loading = false;

  if (response && response.data) {
    getEvaluationsDetail.value = [response.data];
    if (getEvaluationsDetail.value.length > 0) {
      const element = document.getElementById("BottomPage");
      if (element) {
        element.scrollIntoView({ behavior: "smooth", block: "end" });
      }
    }
  } else {
    checkDataDetail.value = true;
  }
};

const dateTime = (date) => {
  var getDate = ref("");
  if (date) {
    getDate.value = moment(date).format("DD-MM-YYYY");
    return getDate.value;
  } else {
    return getDate.value;
  }
};

const showPopupEdit = (data) => {
  itemEdit.value = [];
  itemEdit.value = data;
  originItem.value = { ...data };
};

const saveItem = async () => {
  if (evaluationQueue.value && evaluationQueue.value.length) {
    const channel_type_check_list_beans = evaluationQueue.value;
    loaddingStore.loading = true;
    const response = await axios.post("/api/evaluation-config/edit", {
      action: "EDIT",
      channel_type_check_list_beans,
    });
    if (response && response.data && response.data.length) {
      showSaveButton.value = true;
      notify({
        type: "error",
        title: "Edit Error",
        text: response.data,
        duration: 10000,
      });
    } else {
      showSaveButton.value = false;
      notify({
        type: "success",
        title: i18n.global.t("EDIT_SUCCESSS"),
        text: response.data,
        duration: 10000,
      });
    }

    loaddingStore.loading = false;
  }
};

function updateItemToQueue(actions) {
  const monthYear1 = moment(itemEdit.value.date_evaluation1).format("MM-YYYY");
  const monthYear2 = moment(itemEdit.value.date_evaluation2).format("MM-YYYY");
  const monthYear3 = moment(itemEdit.value.date_evaluation3).format("MM-YYYY");
  const monthYear4 = moment(itemEdit.value.date_evaluation4).format("MM-YYYY");
  if (!itemEdit.value.quantity_per_month) {
    notify({
      type: "error",
      title: "Time",
      text: i18n.global.t("PLEASE_SELECT_APPROVAL_SCORE"),
      duration: 200,
    });
    return;
  }
  if (
    itemEdit.value.approval_score <= 0 ||
    itemEdit.value.approval_score > 100 ||
    itemEdit.value.approval_score == null ||
    itemEdit.value.approval_score == ""
  ) {
    notify({
      type: "error",
      title: "Time",
      text: i18n.global.t("APPROVAL_SCORE_NOT"),
      duration: 2000,
    });
    return;
  }
  if (
    itemEdit.value.quantity_per_month == 1 &&
    itemEdit.value.date_evaluation1 != null
  ) {
    itemEdit.value.date_evaluation2 = null;
    itemEdit.value.date_evaluation3 = null;
    itemEdit.value.date_evaluation4 = null;
  } else if (
    itemEdit.value.quantity_per_month == 2 &&
    itemEdit.value.date_evaluation1 != null &&
    itemEdit.value.date_evaluation2 != null
  ) {
    if (
      dateTime(itemEdit.value.date_evaluation1) >=
      dateTime(itemEdit.value.date_evaluation2)
    ) {
      notify({
        type: "error",
        title: "Time",
        text: i18n.global.t("EVALUATION_TIME_MUST_BE_DATE"),
        duration: 200,
      });
      return;
    }
    if (monthYear1 != monthYear2) {
      notify({
        type: "error",
        title: "Time",
        text: i18n.global.t("EVALUATION_TIME_MONTH"),
        duration: 200,
      });
      return;
    }
    itemEdit.value.date_evaluation3 = null;
    itemEdit.value.date_evaluation4 = null;
  } else if (
    itemEdit.value.quantity_per_month == 3 &&
    itemEdit.value.date_evaluation1 != null &&
    itemEdit.value.date_evaluation2 != null &&
    itemEdit.value.date_evaluation3 != null
  ) {
    if (
      dateTime(itemEdit.value.date_evaluation1) >=
        dateTime(itemEdit.value.date_evaluation2) ||
      dateTime(itemEdit.value.date_evaluation1) >=
        dateTime(itemEdit.value.date_evaluation3) ||
      dateTime(itemEdit.value.date_evaluation2) >=
        dateTime(itemEdit.value.date_evaluation3)
    ) {
      notify({
        type: "error",
        title: "Time",
        text: i18n.global.t("EVALUATION_TIME_MUST_BE_DATE"),
        duration: 200,
      });
      return;
    }
    if (
      monthYear1 != monthYear2 ||
      monthYear1 != monthYear3 ||
      monthYear2 != monthYear3
    ) {
      notify({
        type: "error",
        title: "Time",
        text: i18n.global.t("EVALUATION_TIME_MONTH"),
        duration: 200,
      });
      return;
    }

    itemEdit.value.date_evaluation4 = null;
  } else if (
    itemEdit.value.quantity_per_month == 4 &&
    itemEdit.value.date_evaluation1 != null &&
    itemEdit.value.date_evaluation2 != null &&
    itemEdit.value.date_evaluation3 != null &&
    itemEdit.value.date_evaluation4 != null
  ) {
    if (
      dateTime(itemEdit.value.date_evaluation1) >=
        dateTime(itemEdit.value.date_evaluation2) ||
      dateTime(itemEdit.value.date_evaluation1) >=
        dateTime(itemEdit.value.date_evaluation3) ||
      dateTime(itemEdit.value.date_evaluation2) >=
        dateTime(itemEdit.value.date_evaluation3) ||
      dateTime(itemEdit.value.date_evaluation3) >=
        dateTime(itemEdit.value.date_evaluation4) ||
      dateTime(itemEdit.value.date_evaluation1) >=
        dateTime(itemEdit.value.date_evaluation4) ||
      dateTime(itemEdit.value.date_evaluation2) >=
        dateTime(itemEdit.value.date_evaluation4)
    ) {
      notify({
        type: "error",
        title: "Time",
        text: i18n.global.t("EVALUATION_TIME_MUST_BE_DATE"),
        duration: 200,
      });
      return;
    }
    if (
      monthYear1 != monthYear2 ||
      monthYear1 != monthYear3 ||
      (monthYear1 != monthYear4 && monthYear2 != monthYear3) ||
      monthYear2 != monthYear4 ||
      monthYear3 != monthYear4
    ) {
      notify({
        type: "error",
        title: "Time",
        text: i18n.global.t("EVALUATION_TIME_MONTH"),
        duration: 200,
      });
      return;
    }
  } else {
    notify({
      type: "error",
      title: "Time",
      text: i18n.global.t("REQUIRE.DATE"),
      duration: 200,
    });
    return;
  }
  addEvaluationQueue({ ...itemEdit.value });
  actions.onClose();
  showSaveButton.value = true;
}

function myCallback(newPage) {
  searchEvaluation(newPage);
}

function selectBoxBranch() {
  searchData.current_page = 1;
}

function selectBoxChanelType() {
  searchData.current_page = 1;
}

function selectBoxJob() {
  searchData.current_page = 1;
}

function selectBoxDate() {
  searchData.current_page = 1;
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
              @select="selectBoxBranch"
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
              @select="selectBoxChanelType"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("TYPE_EVALUATION") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.job_id"
              :options="listJod.getJobsToPlan || []"
              @select="selectBoxJob"
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
              @select="selectBoxDate"
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
              @select="selectBoxDate"
            ></VueDatePicker>
          </div>
        </div>
      </div>
      <div class="col align-self-start"></div>
    </div>

    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchEvaluation()"
        >
          {{ $t("SEARCH") }}
        </button>
        <button
          class="btn btn-pill"
          style="background-color: #4682b4; color: #fff"
          type="button"
          @click="exportEvaluation"
        >
          {{ $t("EXPORT_DETAIL") }}
        </button>
      </div>
    </div>

    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkListEvalution" class="table-responsive">
        <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
      </fieldset>
      <fieldset v-if="listEvaluations.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead
              class="table-success"
              style="background-color: #24695c; color: #fff"
            >
              <tr>
                <th scope="col">#</th>
                <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
                <th scope="col">
                  {{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}
                </th>
                <th scope="col">{{ $t("EVALUATION") }}</th>
                <th scope="col">{{ $t("CREATE_DATE") }}</th>
                <th scope="col">{{ $t("CREATED_USER") }}</th>
                <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
              </tr>
            </thead>
            <tbody v-for="(item, index) in listEvaluations" :key="item">
              <tr>
                <td>{{ index + 1 + (searchData.current_page - 1) * 10 }}</td>
                <td>{{ item.branch_code }}</td>
                <td>{{ item.channel_type_name }}</td>
                <td>{{ item.job_code }}</td>
                <td>
                  {{ new Date(item.updated_date).toLocaleDateString("en-GB") }}
                </td>
                <td>{{ item.created_by }}</td>
                <td>
                  <div class="buttons">
                    <button
                      class="btn btn-primary btn-xs"
                      @click="
                        getDetail(
                          item.id,
                          item.job_code,
                          item.branch_code
                          // 'details'
                        )
                      "
                    >
                      {{ $t("DETAIL") }}
                    </button>
                    
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="pagination-search">
            <pagination
              v-model="searchData.current_page"
              :records="paginate.totalRecord"
              :per-page="10"
              @paginate="myCallback"
            />
          </div>
        </div>

        <div ref="myDiv" id="details">
          <div v-if="getEvaluationsDetail?.length" class="row p-15">
            <fieldset>
              <legend>{{ $t("EDIT_EVALUATION") }}</legend>
              <fieldset style="margin-bottom: 30px">
                <legend>{{ branchEdit }}</legend>
                <table class="table table-bordered">
                  <thead class="table-success">
                    <tr>
                      <th scope="col">{{ $t("TYPE_EVALUATION") }}</th>
                      <th scope="col">{{ $t("QUANTITY_PER_MONTH") }}</th>
                      <th scope="col">{{ $t("DATE_OF_EVALUTION") }}1</th>
                      <th scope="col">{{ $t("DATE_OF_EVALUTION") }}2</th>
                      <th scope="col">{{ $t("DATE_OF_EVALUTION") }}3</th>
                      <th scope="col">{{ $t("DATE_OF_EVALUTION") }}4</th>
                      <th scope="col">{{ $t("APPROVAL_SCORE") }}</th>
                      <th scope="col">
                        {{ $t("VISIT_PLAN.TABLE.LABEL.ACTION") }}
                      </th>
                    </tr>
                  </thead>
                  <tbody v-for="data in getEvaluationsDetail" :key="data">
                    <tr class="editable-row">
                      <td>{{ getjobCodeEvalution }}</td>
                      <td>{{ data.quantity_per_month }}</td>
                      <td>{{ dateTime(data.date_evaluation1) }}</td>
                      <td>{{ dateTime(data.date_evaluation2) }}</td>
                      <td>{{ dateTime(data.date_evaluation3) }}</td>
                      <td>{{ dateTime(data.date_evaluation4) }}</td>
                      <td>{{ data.approval_score }}</td>
                      <td>
                        <div id="buttonsDetail" class="buttons">
                          <button
                            class="btn btn-primary btn-xs"
                            data-bs-toggle="modal"
                            data-original-title="test"
                            data-bs-target="#exampleModal"
                            @click="showPopupEdit(data)"
                          >
                            <i class="fa fa-edit"></i>
                            <!-- {{ $t('EDIT') }} -->
                          </button>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </fieldset>
              <div v-if="showSaveButton" class="row">
                <div class="text-center">
                  <button
                    class="col-2 btn btn-danger btn-sm"
                    type="button"
                    @click="saveItem()"
                  >
                    {{ $t("SAVE") }}
                  </button>
                </div>
              </div>
            </fieldset>
          </div>
        </div>
      </fieldset>
    </div>
    
    <div id="BottomPage"></div>

    <popupModal modal-id="exampleModal" title="Edit Evaluation">
      <EvaluationItemEdit v-model="itemEdit" />
      <template #modalActions="modalActionProps">
        <button
          v-if="canOk"
          class="btn btn-secondary"
          type="button"
          @click="updateItemToQueue(modalActionProps)">
          {{ $t("SUBMIT") }}
        </button>
      </template>
    </popupModal>

    <popupModalError modal-id="popupError" title="Error">
      <errorEditEvaluation />
    </popupModalError>
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
../../store/getChannelTypeAuditor.js
