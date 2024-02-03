<script setup>
import axios from "axios";
import moment from "moment";
import { ref, watch } from "vue";
import { reactive } from "vue";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import { getBranch } from "../../store/getBranch";
import { useUserStore } from "../../store/UserStore";
// Initialize listBC with an empty array
// const listBC = ref([]);
const userStore = useUserStore();
// const getListBranch = getBranch();
const listBranch = getBranch();
const loaddingStore = useLoaddingStore();
const checkListSurvey = ref(true);
const searchData = reactive({
  from_date: "",
  to_date: "",
  branch_id: { value: "" }, // Initialize as an object
  bc_id: { value: "" }, // Initialize as an object
  status: "",
  user_id: "",
  current_page: 1,
});
const option = [
  {
    title: "Voted",
    value: "1",
  },
  {
    title: "Not voted yet",
    value: "0",
  },
];
// const totalRecord = ref(0);
const fromDate = ref();
const toDate = ref();
const listSurvey = ref([]);
// function formatDate(date) {
//   const day = date.getDate();
//   const month = date.getMonth() + 1;
//   const year = date.getFullYear();
//   return `${day}/${month}/${year}`;
// }
// function selectBoxBranch(selectedValue) {
//   console.log(selectedValue);
//   searchData.br_code = "";
//   const selectedOption = getListBranch.getBranch.find(
//     (option) => option.value === selectedValue
//   );
//   if (selectedOption) {
//     searchData.br_code = selectedOption.code;
//   }
//   console.log(searchData.br_code);
// }
const searchSurvey = async () => {
  // searchData.from_date = moment(fromDate.value).format("DD-MM-yyyy");
  // searchData.to_date = moment(toDate.value).format("DD-MM-yyyy");
  searchData.from_date = fromDate.value
    ? moment(fromDate.value).format("DD/MM/yyyy")
    : "";
  searchData.to_date = toDate.value
    ? moment(toDate.value).format("DD/MM/yyyy")
    : "";
  listSurvey.value = [];
  checkListSurvey.value = true;
  try {
    loaddingStore.loading = true;
    const response = await axios.post("/api/survey/search", {
      branch_id: searchData.branch_id,
      bc_id: searchData.bc_id,
      user_id: searchData.user_id,
      status: searchData.status,
      from_date: searchData.from_date,
      to_date: searchData.to_date,
      current_page: searchData.current_page,
    });
    if (response && response.data.data && response.data.data.length) {
      // const responseData = response.data.data;
      // responseData.from_date = formatDate(new Date(responseData.from_date));
      // responseData.to_date = formatDate(new Date(responseData.to_date));
      listSurvey.value = response.data.data;
      // totalRecord.value = response.data.data.total_record;
      // paginate.total_page = responseData.total_page;
      // paginate.total_record = responseData.total_record;
      console.log(listSurvey.value);
    } else {
      checkListSurvey.value = false;
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
const dateTime = (date) => {
  var getDate = ref("");
  if (date) {
    getDate.value = moment(date).format("DD-MM-YYYY");
    return getDate.value;
  } else {
    return getDate.value;
  }
};
// const paginate = reactive({
//   total_page: "",
//   current_page: "",
//   total_record: "",
// });

function updateSelectedCodeBC(selectedValue) {
  if(selectedValue) {
    const selectedBC = userStore.listBC.find(bc => bc.value === selectedValue);
    if (selectedBC.value != 7282) {
      searchData.bc_code = selectedBC.code;

    }else {
      searchData.bc_code = ""
    }
  }
}
watch(
  () => userStore.listBranch,
  async (newVal) => {
    if (userStore.currentUser.shop_type == "BR") {
      searchData.checkBRBC = true;
    }
    if (newVal && newVal.length) {
      searchData.branch_id.value = newVal[0].value;
      if (userStore.listBC && userStore.listBC.length) {
        searchData.bc_id.value = userStore.listBC[0].value;
      } else {
        userStore.listBC = userStore.listBranch
        searchData.bc_id.value = userStore.listBranch[0].value
        await userStore.getStaff(searchData.bc_id.value);
      }
    }
  }
);
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
            >{{ $t("HOME.SEARCH.COMBOBOX.BC") }}:</label
          >
          <div class="col-sm-8">
            <!-- <Multiselect
              v-model="searchData.bc_id"
              :options="userStore.listBC || []"
              track-by="code"
              label="label"
              @input="selectBoxBranch"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect> -->
            <Multiselect
              v-model="searchData.bc_id"
              :options="userStore.listBC || []"
              :placeholder="$t('SELECT_ONE')"
              track-by="code"
              label="label"
              disabled
              :canClear="false"
              :searchable="false"
              :allow-empty="false"
              @input="updateSelectedCodeBC"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("USER") }}:</label>
          <div class="col-sm-8">
            <input
              type="text"
              class="form-control"
              v-model="searchData.user_id"
              style="height: 33px"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.BTN.CHANNEL") }}:</label
          >
          <div class="col-sm-8">
            <input
              type="text"
              class="form-control"
              v-model="searchData.channel_code"
              style="height: 33px"
            />
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
          <label class="col-sm-4 col-form-label">{{ $t("STATUS") }}:</label>
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.status"
              deselect-label="Can't remove this value"
              track-by="title"
              label="title"
              :placeholder="$t('SELECT_ONE')"
              :options="option"
              :searchable="false"
              :allow-empty="false"
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
          @click="searchSurvey"
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
      <fieldset v-if="!checkListSurvey" class="table-responsive">
        <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
      </fieldset>
      <fieldset v-if="listSurvey.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead
              class="table-success"
              style="background-color: #24695c; color: #fff"
            >
              <tr>
                <th scope="col">#</th>
                <th scope="col">{{ $t("VOTED") }}</th>
                <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
                <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BC") }}</th>
                <th scope="col">{{ $t("HOME.BTN.CHANNEL") }}</th>
                <th scope="col">{{ $t("USER") }}</th>
                <th scope="col">{{ $t("CREATED_DATE") }}</th>
                <th scope="col">{{ $t("RESEARCH_DATE") }}</th>
              </tr>
            </thead>
            <tbody v-for="(item, index) in listSurvey" :key="item">
              <tr>
                <td>{{ (searchData.current_page - 1) * 10 + (index + 1) }}</td>
                <td>{{ item.status === 1 ? "Voted" : "Not Voted Yet" }}</td>
                <td>{{ item.branch_code }}</td>
                <td>{{ item.bc_code }}</td>
                <td>{{ item.channel_code }}</td>
                <td>{{ item.user_code }}</td>
                <td>{{ dateTime(item.created_date) }}</td>
                <td>{{ dateTime(item.survey_date) }}</td>
              </tr>
            </tbody>
          </table>
          <!-- <div class="pagination-search">
            <pagination
              v-model="searchData.current_page"
              :records="totalRecord"
              :per-page="10"
              @paginate="myCallback"
            />
          </div> -->
        </div>
      </fieldset>
    </div>
  </div>
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
