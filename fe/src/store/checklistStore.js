import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore';
import { notify } from "@kyvg/vue3-notification";


export const usechecklistStore = defineStore("ChecklistStore", {
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
                const response = await axios.post("/api/sale-bts-config/import", formData,{
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
                    duration: 100000,
                });

            }
        },
        // async export(searchData){
        //     try {
        //         this.loaddingStore.loading = true;
        //         const { data: res } = await axios.get(`/api/sale-bts-config/download-file?branchId=${searchData.branch_id!=null ? searchData.branch_id : ""}&ChannelTypeId=${searchData.channel_type_id!=null ? searchData.channel_type_id : ""}&jobId=${searchData.job_id!=null ? searchData.job_id : ""}&fromDate=${searchData.from_date!=null ? searchData.from_date : ""}&toDate=${searchData.to_date!=null ? searchData.to_date : ""}`, { responseType: 'blob' });
        //         var fileURL = window.URL.createObjectURL(new Blob([res],{ type: 'application/vnd.ms-excel' }));
        //         window.location = fileURL;
        //         this.loaddingStore.loading = false;
        //     } catch (error) {
        //         this.loaddingStore.loading = false;
        //         console.log(error);
        //         notify({
        //             type: "error",
        //             title: "Get current user",
        //             text: error,
        //             duration: 100000,
        //         });
        //     }
        // },
        async downloadFileTemplate(fileName){
            try {
                this.loaddingStore.loadding = true;
                const { data: res } = await axios.get(`/api/download-template/${fileName}`, { responseType: 'blob' });
                var fileURL = window.URL.createObjectURL(new Blob([res],{ type: 'application/vnd.ms-excel' }));
                window.location = fileURL;
                this.loaddingStore.loading = false;
            } catch (error) {
                this.loaddingStore.loading = false;
                console.log(error);
                notify({
                    type: "error",
                    title: "Get current user",
                    text: error,
                    duration: 100000,
                });
            }
        }
    },
});
