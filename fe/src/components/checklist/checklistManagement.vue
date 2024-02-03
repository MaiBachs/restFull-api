<script setup>
import axios from "axios"
import moment from 'moment'
import { ref ,reactive, computed} from "vue"
import Multiselect from "@vueform/multiselect"
import VueDatePicker from "@vuepic/vue-datepicker"
import useEvaluation from "@/composables/useEvaluation"
import { notify } from "@kyvg/vue3-notification";
import { getBranch } from './../../store/getBranch'
import { getChannelType } from './../../store/getChannelType'
import { getJobsToPlan} from './../../store/getJodChannel'
import { useLoaddingStore } from './../../store/LoaddingStore'
import popupModal from '../../app/popupModal.vue'
import EvaluationItemEdit from '../../components/evaluation/EvaluationItemEdit.vue'


const loaddingStore = useLoaddingStore()
const { addEvaluationQueue, evaluationQueue} = useEvaluation(ref)
const listBranch = getBranch();
const listChannelType = getChannelType();
const listJod = getJobsToPlan();
const listEvaluations = ref([]);
const getEvaluationsDetail = ref([]);
const branchEdit = ref('');
const checkListEvalution = ref(true);
const itemEdit = ref({})
const originItem = ref({})
const showSaveButton = ref(false)
const listItemEditId = ref([])
const checkDataDetail = ref(false)
const searchData = reactive({
  branch_id: "",
  channel_type_id: "",
  job_id: "",
  from_date: "",
  to_date: "",
});
const fromDate = ref(new Date());
const toDate = ref(new Date());

const canOk = computed(() => JSON.stringify(itemEdit.value) !== JSON.stringify(originItem.value))

const searchEvaluation = async () => {
  showSaveButton.value = false;
  listItemEditId.value = [];
  getEvaluationsDetail.value = [];
  listEvaluations.value = [];
  checkListEvalution.value = true
  loaddingStore.loading = true;
  searchData.from_date = moment(fromDate.value).format("DD-MM-yyyy");
  searchData.to_date = moment(toDate.value).format("DD-MM-yyyy");
  const response  = await axios.post('/api/evaluation-config/search', {
    branch_id: searchData.branch_id,
    channel_type_id: searchData.channel_type_id,
    job_id: searchData.job_id,
    from_date: searchData.from_date,
    to_date: searchData.to_date,
  });
  if(response && response.data.evaluations && response.data.evaluations.length) {
    listEvaluations.value = response.data.evaluations
  } else {
    checkListEvalution.value = false
  }
  loaddingStore.loading = false;


};

const getDetail = async (branch_id, channel_type_id, branch_code) => {
  showSaveButton.value = false
  branchEdit.value = branch_code;
  loaddingStore.loading = true;
  const response  = await axios.post('/api/evaluation-config/details', {
    branch_id: branch_id,
    channel_type_id: channel_type_id
  });
  loaddingStore.loading = false;
  if (response && response.data.length) {
    getEvaluationsDetail.value = response.data;
  } else {
    checkDataDetail.value = true
  }
};
const dateTime = (date) => {
  var getDate = ref("");
  if (date) {
    // var todaydate = new Date(date);
    getDate.value = moment(date).format("DD-MM-YYYY");
    return getDate.value;
  } else {
    return getDate.value;
  }
};

const showPopupEdit = (data) => {
  itemEdit.value = data;
  originItem.value = { ...data }
};

const saveItem = async () => {
  if(evaluationQueue.value && evaluationQueue.value.length) {
    const channel_type_check_list_beans = evaluationQueue.value
    loaddingStore.loading = true;
    const response  = await axios.post('/api/evaluation-config/edit', {
      action: "EDIT",
      channel_type_check_list_beans,
    });
    if(response.data && !response.data.error) {
      showSaveButton.value = false
    } else {
      showSaveButton.value = true
      notify({
        type: 'error',
        title: "Time",
        text: response.data.error,
        duration: 500,
      });
    }

    loaddingStore.loading = false;
  }
};

function updateItemToQueue(actions) {
  if(itemEdit.value.quantity_per_month == 1 && itemEdit.value.date_evaluation1 != null) {
    itemEdit.value.date_evaluation2 = null
    itemEdit.value.date_evaluation3 = null
    itemEdit.value.date_evaluation4 = null
  } else if(itemEdit.value.quantity_per_month == 2 && itemEdit.value.date_evaluation1 != null && itemEdit.value.date_evaluation2 != null) {
    itemEdit.value.date_evaluation3 = null
    itemEdit.value.date_evaluation4 = null
  }else if(itemEdit.value.quantity_per_month == 3 && itemEdit.value.date_evaluation1 != null && itemEdit.value.date_evaluation2 != null && itemEdit.value.date_evaluation3 != null ) {
    itemEdit.value.date_evaluation4 = null
  }else {
    notify({
      type: 'error',
      title: "Time",
      text: 'Không được để thời gian trống',
      duration: 200,
    });
    return
  }

  addEvaluationQueue({...itemEdit.value})
  actions.onClose()
  console.log()
  showSaveButton.value = true
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
          >{{ $t("BTS_CODE") }}:</label
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
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          >{{ $t("POLICY") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
                v-model="searchData.job_id"
                :options="listJod.getJobsToPlan|| []"
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
          >{{ $t("STATUS") }}:</label>
          <div class="col-sm-8">
            <Multiselect
                v-model="searchData.job_id"
                :options="listJod.getJobsToPlan|| []"
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
            @click="searchEvaluation"
        >
          {{ $t("SEARCH") }}
        </button>
        <button
            class="btn btn-pill btn-primary-gradien"
            type="button"
        >
          {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
        </button>
      </div>
    </div>

    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkListEvalution" class="table-responsive">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <fieldset v-if="listEvaluations.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
            <tr>
              <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
              <th scope="col">{{ $t("CREATE_DATE") }}</th>
              <th scope="col">{{ $t("CREATED_USER") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
            </tr>
            </thead>
            <tbody v-for="item in listEvaluations" :key="item">
            <tr>
              <td>{{ item.branch_code }}</td>
              <td>{{ item.channel_type_name }}</td>
              <td>{{ dateTime(item.created_date) }}</td>
              <td>{{ item.created_by }}</td>
              <td>
                <div class="buttons">
                  <button
                      class="btn btn-primary btn-xs"
                      @click="
                        getDetail(
                          item.branch_id,
                          item.channel_type_id,
                          item.branch_code,
                        )
                      "
                  >
                    <i class="fa fa-edit"></i>
                    <!-- {{ $t('EDIT') }} -->
                  </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>


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
                  <th scope="col">{{ $t("DATE_OF_EVALUTION") }}</th>
                  <th scope="col">{{ $t("DATE_OF_EVALUTION") }}</th>
                  <th scope="col">{{ $t("DATE_OF_EVALUTION") }}</th>
                  <th scope="col">{{ $t("DATE_OF_EVALUTION") }}</th>
                  <th scope="col">{{ $t("APPROVAL_SCORE") }}</th>
                  <th scope="col">
                    {{ $t("VISIT_PLAN.TABLE.LABEL.ACTION") }}
                  </th>
                </tr>
                </thead>
                <tbody v-for="data in getEvaluationsDetail" :key="data">
                <tr class="editable-row">
                  <td>{{ data.job_code }}</td>
                  <td>{{ data.quantity_per_month }}</td>
                  <td>{{ dateTime(data.date_evaluation1) }}</td>
                  <td>{{ dateTime(data.date_evaluation2) }}</td>
                  <td>{{ dateTime(data.date_evaluation3) }}</td>
                  <td>{{ dateTime(data.dateEvaluation4) }}</td>
                  <td>{{ data.approval_score }}</td>
                  <td>
                    <div class="buttons">
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
      </fieldset>
    </div>

    <popupModal modal-id="exampleModal" title="Edit Evaluation">
      <EvaluationItemEdit v-model="itemEdit" />
      <template #modalActions="modalActionProps">
        <button v-if="canOk" class="btn btn-secondary" type="button" @click="updateItemToQueue(modalActionProps)">
          Ok
        </button>
      </template>
    </popupModal>
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
