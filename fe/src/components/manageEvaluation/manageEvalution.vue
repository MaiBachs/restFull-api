<script setup>
import axios from "axios";
import moment from "moment";
import { ref, reactive } from "vue";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import { getBranch } from "./../../store/getBranch";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import { getChannelTypeChecklist } from "@/store/getChannelTypeChecklist";
import { getParent } from "@/store/getParent";
import { notify } from "@kyvg/vue3-notification";
import Modal from "@/components/common/customModal.vue";
import evaluationManagementEdit from "@/components/manageEvaluation/evaluationManagementEdit.vue";
import PopupShowImage from "@/components/manageEvaluation/popupShowImage.vue";

const loaddingStore = useLoaddingStore();
const listBranch = getBranch();
const searchData = reactive({
  branch_id: "",
  channel_code: "",
  auditor: "",
  channel_type_id: "",
  type_channel: "",
  evaluation: "",
  from_date: "",
  to_date: "",
  // status_evaluation: "",
  page_size: 10,
  id: "",
  evaluation_id: "",
  current_page: 1,
  paging: true,
});
const getManageEvalutionDetail = ref({
  auditor_id: 0,
});
// const fromDate = ref(new Date());
// fromDate.value = moment(new Date()).subtract(1, 'months');
// const toDate = ref(new Date());
const fromDate = ref();
const toDate = ref();
const listManageEvalution = ref([]);
const checkListManageEvalution = ref([]);
const itDetail = ref([]);
// const itemEdit = ref({});
// const originItem = ref({});
// const showSaveButton = ref(false);
const filebase64VisitPlan = ref([]);
const getlistBase64Image = ref([]);
const listImage = ref(false);

function formatDate(date) {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}

const checkFileType = ref("");
const dateTime = (date) => {
  var getDate = ref("");
  if (date) {
    getDate.value = moment(date).format("DD-MM-yyyy");
    return getDate.value;
  } else {
    return getDate.value;
  }
};
const getItemDetail = ref({
  hinh_anh_url: false,
  reason_of_not_ok: false,
  option_of_answer: false,
  job3: false,
});
const handleChannelTypeChange = async (selectedItemId) => {
  console.log(searchData.channel_type_id);
  const selectedItem = listChannelTypeChecklist.listChannelTypeChecklist.find(
    (item) => item.value === selectedItemId
  );
  if (selectedItem) {
    searchData.object_type = selectedItem.object_type;
    console.log(
      `ID: ${searchData.object_type}, Value: ${searchData.channel_type_id}`
    );
  }
  console.log(searchData.object_type);
};

function myCallback(newPage) {
  searchManageEvalution(newPage);
}

const paginate = reactive({
  total_page: "",
  current_page: "",
  total_record: "",
});
const clearChannelType = () => {
  searchData.channel_type_id = null;
};
const checkDataDetail = ref(false);
const searchManageEvalution = async () => {
  itDetail.value = [];
  searchData.from_date = fromDate.value
    ? moment(fromDate.value).format("DD/MM/yyyy")
    : "";
  searchData.to_date = toDate.value
    ? moment(toDate.value).format("DD/MM/yyyy")
    : "";
  listManageEvalution.value = [];
  getManageEvalutionDetail.value = [];
  checkListManageEvalution.value = true;
  try {
    loaddingStore.loading = true;
    const response = await axios.post(
      "/api/evaluation-manage/search-evaluation-manage",
      searchData
    );
    if (
      response &&
      response.data &&
      response.data.evaluation_manage_dtos.length
    ) {
      const responseData = response.data;
      console.log(formatDate(new Date(responseData.start_date)));
      responseData.from_date = formatDate(new Date(responseData.from_date));
      responseData.to_date = formatDate(new Date(responseData.to_date));
      listManageEvalution.value = responseData.evaluation_manage_dtos;
      searchData.current_page = response.data.current_page;
      paginate.total_page = response.data.total_page;
      paginate.total_record = response.data.total_record;
      console.log(response.data.evaluation_manage_dtos);
    } else {
      checkListManageEvalution.value = false;
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
let ReasonVisitPlan = [];
const getDetail = async (item) => {
  chosenTab.value = 1;
  getManageEvalutionDetail.value = [];
  loaddingStore.loading = true;
  try {
    const response = await axios.post("/api/evaluation-manage/detail", {
      visit_plan_id: item.id,
      job_id: item.evaluation_id,
    });
    loaddingStore.loading = false;
    if (response) {
      const itemDetail = {
        hinh_anh_url: response.detail_evaluation_manage_and_action_plan
          ? response.detail_evaluation_manage_and_action_plan.map(
              (response) => response.detail_evaluation_manage_and_action_plan
            )
          : null,
      };
      getManageEvalutionDetail.value = [itemDetail];
      getItemDetail.value = itemDetail;
      console.log(getItemDetail.value);
    } else {
      checkDataDetail.value = true;
    }
    if (item) {
      getManageEvalutionDetail.value = JSON.parse(JSON.stringify(item));
    } else {
      checkDataDetail.value = true;
    }
    itDetail.value = item;
    ReasonVisitPlan = response.data.detail_evaluation_manage_and_action_plan;
    console.log(ReasonVisitPlan);
    console.log(ReasonVisitPlan[0].action_plan);
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
// const getEdit = async (item, data) => {
//   itemEdit.value = [];
//   itemEdit.value = data;
//   originItem.value = { ...data };
//   loaddingStore.loading = true;
//   showSaveButton.value = false;
//   try {
//     const response = await axios.post("/api/evaluation-manage/detail", {
//       visit_plan_id: item.id,
//       job_id: item.evaluation_id,
//     });
//     console.log(item);
//     loaddingStore.loading = false;
//     if (item) {
//       getManageEvalutionDetail.value = JSON.parse(JSON.stringify(item));
//     } else {
//       checkDataDetail.value = true;
//     }
//     ReasonVisitPlan = response.data;
//     console.log(ReasonVisitPlan);
//   } catch (error) {
//     loaddingStore.loading = false;
//     notify({
//       type: "error",
//       title: "Get current user",
//       text: error,
//       duration: 500,
//     });
//   }
// };
async function getImage(url, totalRequest) {
  try {
    loaddingStore.loading = true;
    const response = await axios.get(
      `/api/item-config/getImageFromFtp?filePath=${url}`
    );
    if (response.data && response.data.data) {
      filebase64VisitPlan.value.push(response.data?.data || { content: "" });
      console.log(filebase64VisitPlan.value);
      console.log(totalRequest);
      if (totalRequest == filebase64VisitPlan.value.length) {
        listImage.value = true;
      }
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
async function getPdf(url, totalRequest) {
  try {
    loaddingStore.loading = true;
    const response = await axios.get(
      `/api/item-config/getImageFromFtp?filePath=${url}`
    );
    console.log(response);
    if (response.data && typeof response.data.data) {
      const contentParts = response.data.data.content.split(",");
      const base64PDF = contentParts[1]; // Lấy phần base64 từ chuỗi content

      const pdfDataURI = `data:application/pdf;base64,${base64PDF}`;
      const pdfObject = {
        content: pdfDataURI,
        file_name: response.data.data.file_name,
      };
      filebase64VisitPlan.value.push(pdfObject);

      if (totalRequest == filebase64VisitPlan.value.length) {
        listImage.value = true;
      }
      console.log(filebase64VisitPlan.value);
    } else {
      console.error("Dữ liệu không phải là chuỗi hoặc không tồn tại");
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

const selectedTab = ref(0);

function selectTab(index) {
  selectedTab.value = index;
  console.log("selectedTab value:", selectedTab.value);
  console.log(ReasonVisitPlan[0].detail_evaluations[index]);
}

const chosenTab = ref(0);

function chooseTab(index) {
  chosenTab.value = index;
  console.log("chosenTab value:", chosenTab.value);
}

const listChannelTypeChecklist = getChannelTypeChecklist();
const listParent = getParent();
const option = [
  {
    title: "Done",
    value: "1",
  },
  {
    title: "Need Action Plan",
    value: "2",
  },
  {
    title: "In Process",
    value: "3",
  },
];

async function viewFiles(filePaths) {
  console.log(filePaths);
  filebase64VisitPlan.value = [];
  getlistBase64Image.value = [];
  const imageFiles = [];
  const audioFiles = [];
  const videoFiles = [];
  const pdfFiles = [];
  await filePaths.forEach((filePath) => {
    const fileType = getFileType(filePath);
    console.log(fileType);
    checkFileType.value = fileType;
    switch (fileType) {
      case "image":
        imageFiles.push(filePath);
        break;
      case "audio":
        audioFiles.push(filePath);
        break;
      case "video":
        videoFiles.push(filePath);
        break;
      case "pdf":
        pdfFiles.push(filePath);
        break;
      default:
        // Xử lý khi có các loại file khác
        break;
    }
  });
  if (imageFiles.length > 0) {
    loadFiles(imageFiles, "image");
  } else if (audioFiles.length > 0) {
    loadFiles(audioFiles, "audio");
  } else if (videoFiles.length > 0) {
    loadFiles(videoFiles, "video");
  } else if (pdfFiles.length > 0) {
    loadFiles(pdfFiles, "pdf");
  }
}

async function loadFiles(files, fileType) {
  console.log(files);
  console.log(fileType);
  const loaddingStore = useLoaddingStore();
  try {
    loaddingStore.loading = true;
    switch (fileType) {
      case "image":
        await Promise.all(files.map((img) => getImage(img, files.length)));
        break;
      case "audio":
        await Promise.all(files.map((audio) => getImage(audio, files.length)));
        break;
      case "video":
        await Promise.all(files.map((video) => getImage(video, files.length)));
        break;
      case "pdf":
        await Promise.all(files.map((pdf) => getPdf(pdf, files.length)));
        break;
      default:
        // default
        break;
    }
  } catch (error) {
    console.error(error);
  } finally {
    loaddingStore.loading = false;
  }
}

function getFileType(filePath) {
  const fileExtension = filePath.split(".").pop().toLowerCase();

  if (["png", "jpg", "jpeg", "gif"].includes(fileExtension)) {
    return "image";
  } else if (["mp3", "wav", "ogg", "mpeg"].includes(fileExtension)) {
    return "audio";
  } else if (["mp4", "avi", "mkv", "mpeg"].includes(fileExtension)) {
    return "video";
  } else if (["pdf"].includes(fileExtension)) {
    return "pdf";
  } else {
    return "other";
  }
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
              placeholder="Pick a value"
              :searchable="true"
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
              v-model="searchData.type_channel"
              :options="listChannelTypeChecklist.listChannelTypeChecklist || []"
              :placeholder="$t('SELECT_ONE')"
              deselect-label="Can't remove this value"
              track-by="label"
              label="label"
              :searchable="true"
              :allow-empty="false"
              style="width: 100%"
              @select="handleChannelTypeChange"
              @clear="clearChannelType"
            >
            </Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE") }}:</label
          >
          <div class="col-sm-8">
            <input
              type="text"
              v-model="searchData.channel_code"
              track-by="name"
              style="
                min-height: calc(
                  var(--ms-border-width, 1px) * 2 + var(--ms-font-size, 1rem) *
                    var(--ms-line-height, 0.8) + var(--ms-py, 0.5rem) * 2
                );
                outline: none;
                position: relative;
                width: 100%;
                border: var(--ms-border-width, 1px) solid
                  var(--ms-border-color, #d1d5db);
                border-radius: var(--ms-radius, 4px);
                padding-left: 13px;
                font-size: initial;
                font-weight: 500;
              "
            />
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("AUDITTORS") }}:</label>
          <div class="col-sm-8">
            <input
              type="text"
              v-model="searchData.auditor"
              track-by="name"
              style="
                min-height: calc(
                  var(--ms-border-width, 1px) * 2 + var(--ms-font-size, 1rem) *
                    var(--ms-line-height, 0.8) + var(--ms-py, 0.5rem) * 2
                );
                outline: none;
                position: relative;
                width: 100%;
                border: var(--ms-border-width, 1px) solid
                  var(--ms-border-color, #d1d5db);
                border-radius: var(--ms-radius, 4px);
                padding-left: 13px;
                font-size: initial;
                font-weight: 500;
              "
            />
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("EVALUATION") }}:</label>
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.evaluation"
              :placeholder="$t('SELECT_ONE')"
              deselect-label="Can't remove this value"
              track-by="label"
              label="label"
              :searchable="true"
              :allow-empty="false"
              style="width: 100%"
              :options="listParent.listParent || []"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("STATUS") }}:</label>
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.status_evaluation"
              deselect-label="Can't remove this value"
              track-by="title"
              label="title"
              :options="option"
              :placeholder="$t('SELECT_ONE')"
              :searchable="false"
              :allow-empty="false"
            >
            </Multiselect>
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

      <div class="col align-self-start"></div>
    </div>

    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchManageEvalution"
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
      <fieldset v-if="!checkListManageEvalution" class="table-responsive">
        <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
      </fieldset>
      <fieldset v-if="listManageEvalution.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead
              class="table-success"
              style="background-color: #24695c; color: #fff"
            >
              <tr>
                <th scope="col">#</th>
                <th scope="col">{{ $t("EVALUATION") }}</th>
                <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
                <th scope="col">
                  {{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}
                </th>
                <th scope="col">
                  {{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE") }}
                </th>
                <th scope="col">{{ $t("AUDITTORS") }}</th>
                <th scope="col">{{ $t("SCORE") }}</th>
                <th scope="col">{{ $t("PERIOD") }}</th>
                <th scope="col">{{ $t("SCHEDULED_DATE") }}</th>
                <th scope="col">{{ $t("STATUS") }}</th>
                <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
              </tr>
            </thead>
            <tbody v-for="(item, index) in listManageEvalution" :key="item">
              <tr>
                <td>{{ (searchData.current_page - 1) * 10 + (index + 1) }}</td>
                <td>{{ item.evaluation_name }}</td>
                <td>{{ item.branch_code }}</td>
                <td>{{ item.type_channel }}</td>
                <td>{{ item.channel_code }}</td>
                <td>{{ item.auditor_code }}</td>
                <td>{{ item.score != null ? item.score + "%" : null }}</td>
                <td>{{ item.period }}</td>
                <td>{{ dateTime(item.scheduled_date) }}</td>
                <td>
                  {{
                    item.status_evaluation == 1
                      ? "Done"
                      : item.status_evaluation == 2
                      ? "Need Action Plan"
                      : item.status_evaluation == 3
                      ? "In Process"
                      : ""
                  }}
                </td>
                <td>
                  <div class="buttons">
                    <button
                      class="btn btn-primary btn-xs"
                      @click="getDetail(item)"
                    >
                      <i class="icofont icofont-list"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="pagination-search">
            <pagination
              v-model="searchData.current_page"
              :records="paginate.total_record"
              :per-page="10"
              @paginate="myCallback"
            />
          </div>
        </div>
      </fieldset>
    </div>
    <div v-if="itDetail.id != null" class="row p-15">
      <fieldset>
        <div class="card">
          <div class="card-body">
            <div class="tabbed-card">
              <ul
                class="nav nav-tabs nav-primary"
                id="pills-clrtab"
                role="tablist"
              >
                <li class="nav-item">
                  <a
                    class="nav-link"
                    @click="chooseTab(1)"
                    :class="{ active: chosenTab === 1 }"
                    >Evaluation</a
                  >
                </li>

                <!-- comment -->

                <li
                  v-if="ReasonVisitPlan[0].action_plan !== null"
                  class="nav-item"
                >
                  <a
                    class="nav-link"
                    @click="chooseTab(2)"
                    :class="{ active: chosenTab === 2 }"
                    >Action Plan</a
                  >
                </li>
              </ul>
              <div class="tab-content2" style="padding: 20px">
                <div v-if="chosenTab === 1">
                  <div class="row col-md-12">
                    <div class="row">
                      <div class="col-md-1">{{ $t("AUDITTORS") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.auditor_code }}
                      </div>
                      <div class="col-md-2">{{ $t("SCORE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.score }}
                      </div>
                      <div class="col-md-2">{{ $t("ACTION_PLAN") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.action_plan }}
                      </div>
                      <!-- <div class="col-md-1">{{ $t("STATUS") }}:</div> -->
                      <!-- <div class="col-md-2">
                        {{ itDetail.status_evaluation }}
                      </div> -->
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">
                        {{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:
                      </div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.branch_code }}
                      </div>
                      <div class="col-md-2">
                        {{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:
                      </div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.type_channel }}
                      </div>
                      <div class="col-md-2">
                        {{
                          $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE")
                        }}:
                      </div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.channel_code }}
                      </div>
                      <div class="col-md-1"></div>
                      <div class="col-md-2"></div>
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">{{ $t("EVALUATION") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.evaluation_name }}
                      </div>
                      <div class="col-md-2">{{ $t("SCHEDULE_DATE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ dateTime(itDetail.scheduled_date) }}
                      </div>
                      <div class="col-md-2">{{ $t("EVALUATION_DATE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ dateTime(itDetail.visit_time) }}
                      </div>
                      <div class="col-md-1"></div>
                      <div class="col-md-2"></div>
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">{{ $t("EVIDENCE") }}:</div>
                      <div class="col-md-2" style="color: blue">null</div>
                      <div class="col-md-2">{{ $t("REVIEW_DATE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ dateTime(itDetail.review_date) }}null
                      </div>
                      <div class="col-md-2">{{ $t("REVIEWING_USER") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.reviewing_user }}null
                      </div>
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">{{ $t("STATUS") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{
                          itDetail.status_evaluation === 1
                            ? $t("Done")
                            : itDetail.status_evaluation === 2
                            ? $t("Need Action Plan")
                            : itDetail.status_evaluation === 3
                            ? $t("In Process")
                            : $t("Unknown Status")
                        }}
                      </div>
                      <div class="col-md-2"></div>
                      <div class="col-md-2" style="color: blue"></div>
                      <div class="col-md-2"></div>
                      <div class="col-md-2" style="color: blue"></div>
                    </div>
                  </div>
                  <div class="card">
                    <div class="card-body">
                      <div class="tabbed-card">
                        <ul
                          class="nav nav-tabs nav-primary"
                          id="pills-clrtab"
                          role="tablist"
                        >
                          <li
                            style="
                              display: flex;
                              overflow-x: auto;
                              overflow-y: hidden;
                            "
                            v-for="it3m in ReasonVisitPlan"
                            :key="it3m"
                          >
                            <a
                              v-for="(tab, index) in it3m.detail_evaluations"
                              :key="tab.staff_of_channel"
                              @click="selectTab(index)"
                              :class="{ active: selectedTab === index }"
                              class="nav-link"
                              id="pills-clrhome-tab"
                              data-bs-toggle="pill"
                              :href="'#pills-' + tab.staff_of_channel"
                              role="tab"
                              :aria-controls="'pills-' + tab.staff_of_channel"
                              aria-selected="true"
                              style="font-size: larger"
                              >{{ tab.staff_of_channel }}</a
                            >
                          </li>
                        </ul>
                        <div class="tab-content">
                          <div v-if="selectedTab !== null">
                            <div
                              v-for="(itemm, index) in ReasonVisitPlan[0]
                                .detail_evaluations"
                              :key="index"
                            >
                              <div v-if="selectedTab === index">
                                <!-- Content for the selected tab -->
                                <div>
                                  <template
                                    v-if="
                                      itemm.reason_of_staff_not_evaluated !==
                                      null
                                    "
                                  >
                                    <fieldset style="margin-top: 20px">
                                      {{ $t("Reason") }}:
                                      {{ itemm.reason_of_staff_not_evaluated }}
                                    </fieldset>
                                  </template>
                                </div>
                                <div
                                  v-for="item111 in itemm.reason_of_visit_plan_map_responses"
                                  :key="item111"
                                >
                                  <p
                                    style="
                                      font-size: larger;
                                      font-weight: 700;
                                      margin-left: 18px;
                                      margin-top: 20px;
                                    "
                                  >
                                    {{ item111.job_name2 }}
                                  </p>
                                  <fieldset>
                                    <div
                                      v-for="item1111 in item111.reason_of_visit_plan_map_responses"
                                      :key="item1111"
                                    >
                                      <p style="margin-left: 20px">
                                        {{ item1111.job_name3 }}
                                      </p>
                                      <fieldset>
                                        <div>
                                          {{ $t("OPTIONS_OF_ANSWER") }}:{{
                                            item1111.option_of_answer
                                          }}
                                        </div>
                                        <div
                                          v-if="
                                            item1111.option_of_answer !==
                                              'OK' &&
                                            item1111.option_of_answer !== 'NA'
                                          "
                                        >
                                          {{ $t("REASON_OF_NOT_OK") }}:{{
                                            item1111.reason_of_not_ok
                                          }}
                                        </div>
                                        <div
                                          v-if="item1111.file_paths.length > 0"
                                        >
                                          <div
                                            v-if="
                                              item1111.file_paths.some(
                                                (path) =>
                                                  path.endsWith('jpg') ||
                                                  path.endsWith('png') ||
                                                  path.endsWith('jpeg')
                                              )
                                            "
                                          >
                                            {{ $t("PHOTO") }}:
                                            <button
                                              v-if="
                                                item1111.file_paths.length > 0
                                              "
                                              @click="
                                                viewFiles(item1111.file_paths)
                                              "
                                              style="
                                                padding: 0px;
                                                border: none;
                                                background: transparent;
                                                color: darkcyan;
                                                text-decoration-line: underline;
                                              "
                                            >
                                              {{ $t("CLICK_TO_VIEW") }}
                                            </button>
                                          </div>
                                          <div
                                            v-if="
                                              item1111.file_paths.some((path) =>
                                                path.endsWith('mp3')
                                              )
                                            "
                                          >
                                            {{ $t("AUDIO") }}:
                                            <button
                                              v-if="
                                                item1111.file_paths.length > 0
                                              "
                                              @click="
                                                viewFiles(item1111.file_paths)
                                              "
                                              style="
                                                padding: 0px;
                                                border: none;Foto: 
                                                background: transparent;
                                                color: darkcyan;
                                                text-decoration-line: underline;"
                                            >
                                              {{ $t("CLICK_TO_VIEW") }}
                                            </button>
                                          </div>
                                          <div
                                            v-if="
                                              item1111.file_paths.some((path) =>
                                                path.endsWith('mp4')
                                              )
                                            "
                                          >
                                            {{ $t("VIDEO") }}:
                                            <button
                                              v-if="
                                                item1111.file_paths.length > 0
                                              "
                                              @click="
                                                viewFiles(item1111.file_paths)
                                              "
                                              style="
                                                padding: 0px;
                                                border: none;
                                                background: transparent;
                                                color: darkcyan;
                                                text-decoration-line: underline;
                                              "
                                            >
                                              {{ $t("CLICK_TO_VIEW") }}
                                            </button>
                                          </div>
                                          <div
                                            v-if="
                                              item1111.file_paths.some((path) =>
                                                path.endsWith('pdf')
                                              )
                                            "
                                          >
                                            {{ $t("DOCUMENT") }}:
                                            <button
                                              v-if="
                                                item1111.file_paths.length > 0
                                              "
                                              @click="
                                                viewFiles(item1111.file_paths)
                                              "
                                              style="
                                                padding: 0px;
                                                border: none;
                                                background: transparent;
                                                color: darkcyan;
                                                text-decoration-line: underline;
                                              "
                                            >
                                              {{ $t("CLICK_TO_VIEW") }}
                                            </button>
                                          </div>
                                          <!--                                          <button-->
                                          <!--                                              v-if="item1111.file_paths.length > 0"-->
                                          <!--                                              @click="viewFiles(item1111.file_paths)"-->
                                          <!--                                              style="-->
                                          <!--                                                padding: 0px;-->
                                          <!--                                                border: none;-->
                                          <!--                                                background: transparent;-->
                                          <!--                                                color: darkcyan;-->
                                          <!--                                                text-decoration-line: underline;"-->
                                          <!--                                          >-->
                                          <!--                                            {{ $t("CLICK_TO_VIEW") }}-->
                                          <!--                                          </button>-->
                                        </div>
                                      </fieldset>
                                    </div>
                                  </fieldset>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- comment -->
                <div v-if="chosenTab === 2">
                  <div class="row col-md-12">
                    <div class="row">
                      <div class="col-md-1">{{ $t("AUDITTORS") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.auditor_code }}
                      </div>
                      <div class="col-md-2">{{ $t("SCORE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.score }}
                      </div>
                      <div class="col-md-2">{{ $t("ACTION_PLAN") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{
                          ReasonVisitPlan[0].action_plan === null ? "No" : "Yes"
                        }}
                      </div>
                      <!-- <div class="col-md-1">{{ $t("STATUS") }}:</div>
                      <div class="col-md-2">
                        {{ itDetail.status_evaluation }}
                      </div> -->
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">
                        {{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:
                      </div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.branch_code }}
                      </div>
                      <div class="col-md-2">
                        {{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:
                      </div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.type_channel }}
                      </div>
                      <div class="col-md-2">
                        {{
                          $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE")
                        }}:
                      </div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.channel_code }}
                      </div>
                      <div class="col-md-1"></div>
                      <div class="col-md-2"></div>
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">{{ $t("EVALUATION") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.evaluation_name }}
                      </div>
                      <div class="col-md-2">{{ $t("SCHEDULE_DATE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ dateTime(itDetail.scheduled_date) }}
                      </div>
                      <div class="col-md-2">{{ $t("EVALUATION_DATE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ dateTime(itDetail.visit_time) }}
                      </div>
                      <div class="col-md-1"></div>
                      <div class="col-md-2"></div>
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">{{ $t("EVIDENCE") }}:</div>
                      <div class="col-md-2" style="color: blue">null</div>
                      <div class="col-md-2">{{ $t("REVIEW_DATE") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ dateTime(itDetail.review_date) }}null
                      </div>
                      <div class="col-md-2">{{ $t("REVIEWING_USER") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{ itDetail.reviewing_user }}null
                      </div>
                    </div>
                    <div class="row" style="margin-top: 10px">
                      <div class="col-md-1">{{ $t("STATUS") }}:</div>
                      <div class="col-md-2" style="color: blue">
                        {{
                          itDetail.status_evaluation === 1
                            ? $t("Done")
                            : itDetail.status_evaluation === 2
                            ? $t("Need Action Plan")
                            : itDetail.status_evaluation === 3
                            ? $t("In Process")
                            : $t("Unknown Status")
                        }}
                      </div>
                      <div class="col-md-2"></div>
                      <div class="col-md-2" style="color: blue"></div>
                      <div class="col-md-2"></div>
                      <div class="col-md-2" style="color: blue"></div>
                    </div>
                  </div>
                  <div style="text-align: center" class="buttons">
                    <button
                      class="btn btn-primary btn-xl"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleEvaluationManagementEdit"
                      style="margin-left: 5px"
                    >
                      {{ $t("EDIT") }}<!-- <i class="icofont icofont-pencil-alt-5"></i> -->
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </div>
  </div>
  <Modal v-model="listImage" title="VIEW MEDIA">
    <PopupShowImage
      :filebase64VisitPlan="filebase64VisitPlan"
      :checkFileType="checkFileType"
    />
  </Modal>
  <div>
    <evaluationManagementEdit
      @clearValue="clearListPolicy"
      :itemAdd="getManageEvalutionDetail"
      v-model="getManageEvalutionDetail"
      :item="item"
      :auditor_id="getManageEvalutionDetail.auditor_id"
      :options="options"
      :ReasonVisitPlan="ReasonVisitPlan"
    />
  </div>
</template>

<style lang="scss" scoped>
.nav-tabs .nav-link:active,
.nav-tabs .nav-link:focus,
.nav-tabs .nav-link:hover {
  background-color: #e0e0e0;
  color: #333;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.nav .nav-tabs .nav-primary.active {
  background-color: #1d2024;
}

.multiselect {
  max-height: 35px;
  min-height: 30px;
}

.dp__input_wrap {
  height: 35px;
  display: flex;
}

.fieldset {
  padding: 18px !important;
  margin: 0 2px;
  border: 1px solid #c0c0c0 !important;
  border-bottom: none !important;
  border-right: none !important;
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
.nav-tabs .nav-link.active,
.nav-tabs .nav-item.show .nav-link {
  color: #fff;
  background: #24695c;
}
</style>
