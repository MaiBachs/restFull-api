import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore';
import { notify } from "@kyvg/vue3-notification";


export const useEvalutionStore = defineStore("EvalutionStore", {
  state: () => {
    return {
      editItem: {},
      quantitySuccess: {},
      fileNameError: {},
      viewFileContent: {},
      isFileError: false,
      loaddingStore : useLoaddingStore(),
    };
  },
  getters: {},
  actions: {

    passEditItem(item) {
      this.editItem = item;
    },

  async importConfig(formData) {
      const loaddingStore = useLoaddingStore()
      try {
          loaddingStore.loading = true;
          const response = await axios.post("/api/evaluation-config/import", formData,{
              headers: {
              'Content-Type': 'multipart/form-data', 
            },
          });

          if( (response.data[0] && response.data[1])){
            this.quantitySuccess = response.data[0].body.status.code;
            this.fileNameError = response.data[1].body.status.code;
          }else{
            this.viewFileContent = response.data[2].body.data;
            this.isFileError = false;
          }
          loaddingStore.loading = false;
          if(this.fileNameError.length){
            this.isFileError = true;
          }else{
            this.isFileError = false;
          }
      }
      catch (error) {
          loaddingStore.loading = false;
          console.log(error)
          notify({
              type: 'error',
              title: "Get current user",
              text: error,
              duration: 500,
            });
          
      }
  },
  async export(searchData){
    try {
      this.loaddingStore.loading = true;
      await axios.get(`/api/evaluation-config/download-file?branchId=${searchData.branch_id!=null ? searchData.branch_id : ""}&ChannelTypeId=${searchData.channel_type_id!=null ? searchData.channel_type_id : ""}&jobId=${searchData.job_id!=null ? searchData.job_id : ""}&fromDate=${searchData.from_date!=null ? searchData.from_date : ""}&toDate=${searchData.to_date!=null ? searchData.to_date : ""}`, { responseType: 'blob' }).then((response) => {
        const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        response.headers['content-disposition',`attachment; filename="Template_items_export.xls"`];
        link.setAttribute('download', "Template_evaluation_export_detail.xls");
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
  async downloadFileTemplate(fileName,statusFileError){
    try {
      this.loaddingStore.loadding = true;
      const { data: res ,headers} = await axios.get(`/api/download-template/${fileName}/${statusFileError}`, { responseType: 'blob' });
      const contentDispositionHeader = headers['content-disposition'];
      const match = contentDispositionHeader.match(/filename="(.+)"/);
      const filename = match ? match[1] : 'downloaded.xlsx';
      var fileURL = window.URL.createObjectURL(new Blob([res],{ type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }));

      var a = document.createElement('a');
      a.href = fileURL;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      this.loaddingStore.loading = false;
    } catch (error) {
      this.loaddingStore.loading = false;
      console.log(error);
      notify({
        type: "error",
        title: "Get current user",
        text: error,
        duration: 1800,
      });
    }
  }
  },
});
