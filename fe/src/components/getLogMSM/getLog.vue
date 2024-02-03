<script setup>
import axios from "axios";
import { ref, reactive } from "vue";
// import Multiselect from "@vueform/multiselect";
import { useLoaddingStore } from "./../../store/LoaddingStore";
// import VueDatePicker from "@vuepic/vue-datepicker"
// import { getlistLog } from "./../../store/getlistLog";
// const listgetLog = getlistLog();
// console.log(listgetLog);
const listLog = ref([]);
const checkListLog = ref([]);
const loaddingStore = useLoaddingStore();
const searchData = reactive({
  select_log: "",
  dateGetLog: "",
  viewLog: "",
  current_page: 1,
  paging: true,
});
const searchLog = async () => {
  loaddingStore.loading = true;
  listLog.value = [];
  checkListLog.value = true;

  try {
    const response = await axios.get(
      `/api/log/view-log-channel-checklist?currentPage=${searchData.current_page}`
    );
    if (
      response &&
      response.data &&
      response.data.data.file_names.length
    ) {
      listLog.value = response.data.data.file_names;
      searchData.current_page = response.data.data.current_page;
      paginate.total_page = response.data.data.total_page;
      paginate.total_record = response.data.data.total_record;
      console.log(response.data.data);
    } else {
      checkListLog.value = false;
    }
   
    loaddingStore.loading = false;
  } catch (error) {
    // Handle error
  } finally {
    loaddingStore.loading = false;
  }
};
// const dateGetLog = ref(new Date());
// function formatDate(date) {
//   const day = date.getDate();
//   const month = date.getMonth() + 1;
//   const year = date.getFullYear();
//   return `${year}-${month}-${day}`;
// }
const downloadLog = async (item) => {
  loaddingStore.loading = true;
  try {
    const response = await axios.get(
      `/api/log/download-log-channel-checklist?fileNameDownload=${item}`
    );
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", `${item}.log`);
    link.style.display = "none";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    // Error
  } finally {
    loaddingStore.loading = false;
  }
};
// const viewLog = async (item) => {
//   loaddingStore.loading = true;
//   try {
//     const response = await axios.get(
//       `/api/log/download-log-channel-checklist?fileNameDownload=${item}`
//     );
//     const url = window.URL.createObjectURL(new Blob([response.data]));
//     const link = document.createElement("a");
//     link.href = url;
//     link.target = "_blank"; // set target "_blank"new tab 
//     link.style.display = "none";
//     document.body.appendChild(link);
//     link.click();
//     document.body.removeChild(link);
//   } catch (error) {
//     // Xử lý lỗi
//   } finally {
//     loaddingStore.loading = false;
//   }
// };
function myCallback(newPage) {
  searchLog(newPage);
}
const paginate = reactive({
  total_page: "",
  currentPage: 1,
  total_record: "",
});

</script>

<template>
   <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchLog()"
        >
          {{ $t("SEARCH") }}
        </button>
      </div>
    </div>
  <fieldset v-if="listLog.length" class="table-responsive">
    <div class="table-responsive">
      <table class="table table-bordered table-hover">
        <thead
          class="table-success"
          style="background-color: #24695c; color: #fff"
        >
          <tr>
            <th scope="col" style="width: 50px; text-align: center;">#</th>
            <th scope="col" style="text-align: center;">{{ $t("LOG") }}</th>
            <th scope="col" style="text-align: center;">{{ $t("DATE") }}</th>
            <th scope="col" style="width: 300px; text-align: center;">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
          </tr>
        </thead>
        <tbody v-for="(item, index) in listLog" :key="item">
          <tr>
            <td style="text-align: center;">{{ (searchData.current_page - 1) * 10 + (index + 1) }}</td>
            <td style="text-align: center;">{{ item }}</td>
            <td style="text-align: center;">{{ item.split('.')[1] }}</td>
            <td style="text-align: center;">
              <div class="buttons">
                <button
                  class="btn btn-primary btn-xs"
                  style="background-color: #4682b4; color: #fff;"
                  type="button"
                  @click="downloadLog(item)"
                >
                <i class="icofont icofont-arrow-down"></i>
                </button>
                <!-- <button
                  class="btn btn-primary btn-xs"
                  style="background-color: #4682b4; color: #fff; margin-left: 10px;"
                  type="button"
                  @click="viewLog(item)"
                >
                <i class="icofont icofont-search-alt-1"></i>
                </button> -->
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
  <!-- <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col-sm-6">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("LOG") }}:</label>
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.select_log"
              :options="listgetLog.listgetLog.reverse() || []"
              :searchable="true"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col-sm-6">
        <div class="mb-3 row">
          <button
            class="btn btn-pill"
            style="background-color: #4682b4; color: #fff; width: 200px"
            type="button"
            @click="downloadLog(item)"
          >
            {{ $t("DOWNLOAD_LOG") }}
          </button>
        </div>
      </div>
    </div>
  </div> -->
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
.suggested-list {
  position: absolute;
  background-color: #fff;
  border: 1px solid #ccc;
  max-height: 150px;
  overflow-y: auto;
  list-style: none;
  padding: 0;
  margin: 0;
  z-index: 100;
}
.input-search-plan {
  width: 320px;
  border: 1px solid #cdcdcd;
  height: 30px;
}
</style>
