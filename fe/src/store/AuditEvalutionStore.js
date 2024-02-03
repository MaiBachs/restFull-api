import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore';
import { notify } from "@kyvg/vue3-notification";


export const useAuditEvalutionStore = defineStore("AuditStore", {
  state: () => {
    return {
      paginate: {
        "totalPage": "",
        "currentPage": "",
        "totalRecord":""
      },
      dataSearch: [],
      loaddingStore : useLoaddingStore(),
      listAuditor: {},
    };
  },
  getters: {},
  actions: {

    passEditItem(item) {
      this.editItem = item;
    },
    async export(searchData){
      try {
        this.loaddingStore.loading = true;
        await axios.get(`/api/auditor-config/download-file?branchId=${searchData.branch_id !=null ? searchData.branch_id : ""}&channelTypeId=${searchData.channel_type_id!=null ? searchData.channel_type_id : ""}&auditorId=${searchData.auditor_id!=null ? searchData.auditor_id : ""}&shopChannelId=${searchData.shop_channel_id!=null ? searchData.shop_channel_id : ""}&fromDate=${searchData.from_date!=null ? searchData.from_date : ""}&toDate=${searchData.to_date!=null ? searchData.to_date : ""}`, { responseType: 'blob' }).then((response) => {
          const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          response.headers['content-disposition',`attachment; filename="Template_items_export.xls"`];
          link.setAttribute('download', "Template_audit_export_detail.xls");
          document.body.appendChild(link);
          link.click();
          link.remove();
        });
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    },
    async getAuditor(){
      try {
        this.loaddingStore.loading = true;
        const auditor = await axios.get('/api/auditor-config/staff-auditor');
        if(auditor.data && auditor.data.length) {
          this.listAuditor  = auditor.data.map(function (o) {
              return {
                value: o.staff_id,
                label: o.staff_code,
              };
          })
        }
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
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
