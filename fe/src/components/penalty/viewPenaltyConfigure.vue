<script setup>
import axios from "axios";
import moment from "moment";
import { ref } from "vue";
import { reactive } from "vue";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import { getJobsToPlan } from "./../../store/getJodChannel";
import { getUser } from "./../../store/getUser";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import viewEditPenalty from "@/components/penalty/viewEditPenalty.vue";
// import Modal from '@/components/common/customModal.vue' 
// import viewDetailPenalty from "@/components/penalty/viewDetailPenalty.vue"
const showPopupEdit = ref(false)
const loaddingStore = useLoaddingStore();
const listgetUser = getUser();
const listJod = getJobsToPlan();
const checkListPenalty = ref(true);
const checkDataDetail = ref(false);
// var listPenaltyDetail = ref([]);
const searchData = reactive({
  evaluation_id: "",
  user_type_id: "",
  from_date: "",
  to_date: "",
  current_page: 1,
});
const getPenaltyDetail = ref([]);
const totalRecord = ref(0);
const fromDate = ref();
const toDate = ref();
const listPenalty = ref([]);
function formatDate(date) {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}
const searchPenalty = async () => {
  // searchData.from_date = moment(fromDate.value).format("DD-MM-yyyy");
  // searchData.to_date = moment(toDate.value).format("DD-MM-yyyy");
  searchData.from_date = fromDate.value
    ? moment(fromDate.value).format("DD/MM/yyyy")
    : "";
  searchData.to_date = toDate.value
    ? moment(toDate.value).format("DD/MM/yyyy")
    : "";
  listPenalty.value = [];
  getPenaltyDetail.value = [];
  checkListPenalty.value = true;
  try {
    loaddingStore.loading = true;
    const response = await axios.post("/api/penalty/search", {
      evaluation_id: searchData.evaluation_id,
      user_type_id: searchData.user_type_id,
      from_date: searchData.from_date,
      to_date: searchData.to_date,
      current_page: searchData.current_page,
      paging: true,
    });
    if (
      response &&
      response.data.data.penalty_list &&
      response.data.data.penalty_list.length
    ) {
      const responseData = response.data.data;
      responseData.from_date = formatDate(new Date(responseData.from_date));
      responseData.to_date = formatDate(new Date(responseData.to_date));
      listPenalty.value = response.data.data.penalty_list;
      totalRecord.value = response.data.data.total_record;
      paginate.total_page = responseData.total_page;
      paginate.total_record = responseData.total_record;
      console.log(response.data.data);
      console.log(listPenalty.value);
    } else {
      checkListPenalty.value = false;
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
};
// const getPenaltyDetail = ref({
//   job_id: 0,
//   user_type: 0,
// });
const downloadFile = async () => {
  searchData.from_date = moment(fromDate.value).format("DD-MM-yyyy");
  searchData.to_date = moment(toDate.value).format("DD-MM-yyyy");
  const url = `http://localhost:8090/api/item-config/download-search?groupId=${
    searchData.group_id != null ? searchData.group_id : ""
  }&channelTypeId=${
    searchData.channel_type != null ? searchData.channel_type : ""
  }&evaluationId=${
    searchData.job_id != null ? searchData.job_id : ""
  }&fromDate=${
    searchData.from_date != null ? searchData.from_date : ""
  }&toDate=${searchData.to_date != null ? searchData.to_date : ""}`;
  window.location = url;
};
async function getDetail(item) {
  loaddingStore.loading = true;
  getPenaltyDetail.value = [];
  console.log(item);
  // try {
  const response = await axios.get("/api/penalty/detail", {
    params: {
      penaltyId: item.id,
    },
  });
  if (response && response.data.data) {
    getPenaltyDetail.value = [response.data.data];
    if (getPenaltyDetail.value.length > 0) {
      console.log(getPenaltyDetail.value);
      loaddingStore.loading = false;
      const element = document.getElementById("BottomPage");
      if (element) {
        element.scrollIntoView({ behavior: "smooth", block: "end" });
      }
    }
  } else {
    checkDataDetail.value = true;
  }
}
function EditPenalty() {
  showPopupEdit.value = true
}
//     if (item) {
//       getPenaltyDetail.value = item;
//       loaddingStore.loading = false;
//     } else {
//       checkDataDetail.value = true;
//     }
//     listPenaltyDetail.value = response.data.data;
//     console.log(listPenaltyDetail.value);
//   } catch (error) {
//     loaddingStore.loading = false;
//     console.error("Error fetching penalty detail:", error);

//     // Handle the error, e.g., show a notification
//     notify({
//       type: "error",
//       title: "Error fetching penalty detail",
//       text: error.message,
//       duration: 500,
//     });
//   }
// }
// function clearListPenalty() {
//   searchPenalty()
// }
const dateTime = (date) => {
  var getDate = ref("");
  if (date) {
    getDate.value = moment(date).format("DD-MM-YYYY");
    return getDate.value;
  } else {
    return getDate.value;
  }
};
const paginate = reactive({
  total_page: "",
  current_page: "",
  total_record: "",
});
</script>

<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("TYPE_EVALUATION") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.evaluation_id"
              :options="listJod.getJobsToPlan || []"
              track-by="name"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
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
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("USER") }}:</label>
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.user_type_id"
              :options="listgetUser.listgetUser || []"
              track-by="name"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start"></div>
      <div class="col align-self-start"></div>
    </div>
    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchPenalty"
        >
          {{ $t("SEARCH") }}
        </button>
        <button
          class="btn btn-pill"
          style="background-color: #4682b4; color: #fff"
          type="button"
          @click="downloadFile"
        >
          {{ $t("EXPORT_DETAIL") }}
        </button>
      </div>
    </div>
    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkListPenalty" class="table-responsive">
        <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
      </fieldset>
      <fieldset v-if="listPenalty.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead
              class="table-success"
              style="background-color: #24695c; color: #fff"
            >
              <tr>
                <th scope="col">#</th>
                <th scope="col">{{ $t("TYPE_EVALUATION") }}</th>
                <th scope="col">{{ $t("USER") }}</th>
                <th scope="col">{{ $t("CREATOR_USER") }}</th>
                <th scope="col">{{ $t("CREATED_DATE") }}</th>
                <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
              </tr>
            </thead>
            <tbody v-for="(item, index) in listPenalty" :key="item">
              <tr>
                <td>{{ (searchData.current_page - 1) * 10 + (index + 1) }}</td>
                <td>{{ item.evaluation_type }}</td>
                <td>{{ item.user_type }}</td>
                <td>{{ item.created_by }}</td>
                <td>{{ dateTime(item.created_date) }}</td>
                <td>
                  <div class="buttons">
                    <button
                      class="btn btn-primary btn-xs"
                      @click="getDetail(item, data)"
                    >
                      <i class="icofont icofont-ui-note"></i>
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
    <div ref="myDiv" id="details">
      <div v-if="getPenaltyDetail?.length > 0" class="row p-15">
        <fieldset>
          <legend>{{ $t("DETAIL_PENALTY") }}</legend>
          <fieldset>
            <div>
              <div class="row col-md-12 p-b-15">
                <div class="col-md-6 text-center">
                  <div class="row">
                    <div class="col-md-2">{{ $t("EVALUATION") }}:</div>
                    <div class="col-md-4" style="color: blue">
                      {{ getPenaltyDetail[0].evaluation_type }}
                    </div>
                  </div>
                </div>
                <div class="col-md-6 text-center"></div>
              </div>
              <fieldset>
                <div class="row col-md-12 p-b-15">
                  <div class="col-md-6 text-center">
                    <div class="row">
                      <div class="col-md-2">{{ $t("USER") }}:</div>
                      <div class="col-md-4" style="color: blue">
                        {{ getPenaltyDetail[0].user_type }}
                      </div>
                    </div>
                  </div>
                  <div class="col-md-6 text-center"></div>
                </div>

                <fieldset>
                  <legend>{{ $t("DETAIL") }}</legend>
                  <div
                    v-for="item in getPenaltyDetail[0].detail_data"
                    :key="item"
                  >
                    <div class="row col-md-12 p-b-15">
                      <div class="col-md-3 text-center">
                        <div class="row">
                          <div class="col-md-6">
                            <p>{{ item.gravedad }}</p>
                          </div>
                          <div class="col-md-6">
                            <input
                              v-model="item.penalidad"
                              class="form-control-input col-md-7"
                              type="text"
                              style="text-align: center"
                              readonly
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </fieldset>
              </fieldset>
            </div>
          </fieldset>
          <div style="text-align: center" class="buttons">
                    <button
                      class="btn btn-primary btn-xl"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleModalEditPenalty"
                      @click="EditPenalty()"
                      style="margin-left: 5px; margin-top: 20px;"
                    >
                      {{ $t("EDIT") }}
                    </button>
                  </div>
        </fieldset>
      </div>
    </div>
  </div>
  <viewEditPenalty  
  @clearValue="clearListPenalty" 
  v-model="getPenaltyDetail[0]" 
  :listPenaltyDetail="listPenaltyDetail"
></viewEditPenalty>
</template>
<style>
.header-new-page {
  height: 10px;
  margin-bottom: 30px;
}
.d-newpage-none {
  display: none;
}
.multiselect {
  max-height: 35px;
  min-height: 30px;
}
.dp__input_wrap {
  height: 35px;
  display: flex;
}
legend {
  float: initial;
  font-size: 12px !important;
  width: auto;
  border-bottom: none;
  padding: 0px 5px !important;
}
fieldset {
  padding: 18px !important;
  margin: 0 2px;
  border: 1px solid #c0c0c0 !important;
}
.main-edit {
  width: 100%;
  height: 500px;
}
.item-top,
.item-bot {
  display: flex;
  padding: 20px 50px;
}
.item-top-left {
  width: 30%;
}

.item-top-right {
  width: 70%;
}

.item-top-left > legend {
  margin-top: 11px;
}

.item-top-right > legend {
  margin-top: 11px;
}

.btn-radio-item label {
  margin-right: 25px;
}

.custom-control-input[type="checkbox"] {
  width: 13px; /* Độ rộng muốn đặt cho checkbox */
  height: 13px; /* Độ cao muốn đặt cho checkbox */
}
.grave {
  border-collapse: separate; /* Đảm bảo sử dụng border-spacing */
  border-spacing: 0 13px; /* Khoảng cách theo chiều dọc */
}
</style>
../../store/getChannelTypeAuditor
