import { defineStore } from "pinia";
import axios from "axios";
import { useLoaddingStore } from "./LoaddingStore";
import { notify } from "@kyvg/vue3-notification";

export const useItemConfigStore = defineStore("ItemConfigStore", {
  state: () => {
    return {
      quantitySuccess: {},
      fileNameError: {},
      viewFileContent: {},
      isFileError: false,
      dataSearch: {},
      loaddingStore : useLoaddingStore(),
      dataItemDetails: {},
      paginate: {
        totalPage: "",
        currentPage: "",
        totalRecord:""
      },
      errorMessageFlag: false,
      successMessageFlag: false,
      checkListEvalution : true
    };
  },
  getters: {},
  actions: {
    async fetchItemConfigImport(formData) {
      try {
        this.loaddingStore.loading = true;

        const response = await axios.post("/api/item-config/import", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        if(response.data.data != null){
          this.quantitySuccess = response.data.data;
          if(response.data.status.message){
            this.fileNameError = response.data.status.message;
          }
        }else{
          this.quantitySuccess = "success";
          this.fileNameError = null;
        }
        
        if (this.fileNameError) {
          this.successMessageFlag = false
          this.errorMessageFlag = true
        } else {
          this.errorMessageFlag = false
          this.successMessageFlag = true
        }
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
        console.log(error);
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    },

    async viewContentFile(formData) {
      try {
        this.loaddingStore.loading = true;

        const response = await axios.post("/api/item-config/import", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        this.viewFileContent = response.data;
        console.log(response.data);
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
        console.log(error);
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    },

    // async searchItem(formSearch) {
    //   try {
    //     this.checkListEvalution = true
    //     this.loaddingStore.loading = true;
    //     const response = await axios.post(
    //       "/api/item-config/search",
    //       formSearch
    //     );

    //     if(response && response.data.data.item_config_dto_list && response.data.data.item_config_dto_list.length) {
    //         this.checkListEvalution = true
    //         this.dataSearch = response.data.data.item_config_dto_list
    //         this.paginate.currentPage = response.data.data.current_page;
    //         this.paginate.totalPage = response.data.data.total_page;
    //         this.paginate.totalRecord = response.data.data.total_record;
    //     }else{
    //       this.checkListEvalution = false
    //       this.dataSearch = [];
    //     }
    //     this.loaddingStore.loading = false;
    //   } catch (error) {
    //     this.loaddingStore.loading = false;
    //     console.log(error);
    //     notify({
    //       type: "error",
    //       title: "Get current user",
    //       text: error,
    //       duration: 100000,
    //     });
    //   }
    // },
    async details(itemConfigId){
      try {
        this.loaddingStore.loading = true;
        const detail = await axios.get(`/api/item-config/detail-item-config?itemConfigId=${itemConfigId}`);
        if (detail) {
          this.dataItemDetails = detail.data;
          console.log(this.dataItemDetails)
        }else{
          this.dataItemDetails = []
        }
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
        console.log(error);
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    },
    async export(searchData){
      try {
        this.loaddingStore.loading = true;
        await axios.get(`/api/item-config/download-search?groupId=${searchData.group_id!=null ? searchData.group_id : ""}&channelTypeId=${searchData.channel_type_id !=null ? searchData.channel_type_id : ""}&evaluationId=${searchData.evaluation_id!=null ? searchData.evaluation_id : ""}&fromDate=${searchData.from_date!=null ? searchData.from_date : ""}&toDate=${searchData.to_date!=null ? searchData.to_date : ""}`, { responseType: 'blob' }).then((response) => {
          const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          response.headers['content-disposition',`attachment; filename="Template_items_export.xls"`];
          link.setAttribute('download', "Template_items_export.xls");
          document.body.appendChild(link);
          link.click();
          link.remove();
        });
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
        console.log(error);
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    },

    async downloadFileError(fileName){
      await axios.get(`api/download-template/${fileName}/1`, { responseType: 'blob' }).then((response) => {
        const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        response.headers['content-disposition',`attachment; filename="${fileName}.xls"`];
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        link.remove();
      });
    },

    async downloadFile(fileName){
      await axios.get(`api/download-template/${fileName}/0`, { responseType: 'blob' }).then((response) => {
        const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        response.headers['content-disposition',`attachment; filename="${fileName}.xls"`];
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        link.remove();
      });
    },
    
    async downloadFileTemplate(fileName){
      try {
        this.loaddingStore.loading = true;
        await axios.get(`api/item-config/download-template`, { responseType: 'blob' }).then((response) => {
          const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          response.headers['content-disposition',`attachment; filename="${fileName}.xls"`];
          link.setAttribute('download', fileName);
          document.body.appendChild(link);
          link.click();
          link.remove();
        });
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
        console.log(error);
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    },

  },
});
