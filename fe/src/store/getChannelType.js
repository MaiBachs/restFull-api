import { defineStore } from 'pinia';
import axios from "axios"
import { notify } from "@kyvg/vue3-notification";
import { useLoaddingStore } from './LoaddingStore';
export const getChannelType = defineStore("getChannelTypeStore", {
  state: () => {
    return {
      listChannelType: {},
      listAllChannelType: {},
      getListFillPlan: [],
      getListCheckListLevel1: []
    };
  },
  actions: { 
    
    async actionGetChannelType () {
      const loaddingStore = useLoaddingStore();
      loaddingStore.loading = true;
      const channelType = await axios.get('/api/evaluation-config/channel-types');
      if(channelType.data && channelType.data.length) {
        this.listChannelType  = channelType.data.map(function (o) {
          if (o && o.channel_type_id) {
            return {
              value: o.channel_type_id,
              label: o.name,
            };
          }
           
          })
      }
      loaddingStore.loading = false;

    },

    async actionGetListFillPlan (){
      try {
        const { data: fillPlan } = await axios.get(`/api/plan-job/fill-plan`);
            if(fillPlan && fillPlan.length) {
                this.getListFillPlan =  fillPlan.map(function (o) {
                  return {
                    value: o.plan_id,
                    label: o.plan_name,
                  };
                    
                })
                
            }else{
                this.getListFillPlan = []
            }
      } catch (error) {
          notify({
              type: "error",
              title: "Get current user",
              text: error,
              duration: 100000,
          });
      }
    },
    async actionGetListLevle1 (){
      try {
        const { data: checkListLevel1 } = await axios.get(`api/plan-job/fill-checklist-level1-using-assign-checklist`);
            if(checkListLevel1 && checkListLevel1.length) {
                this.getListCheckListLevel1 =  checkListLevel1.map(function (o) {
                  return {
                    value: o.job_id,
                    label: o.name +'('+ o.code +')',
                  };
                    
                })
                
            }else{
                this.getListCheckListLevel1 = []
            }
      } catch (error) {
          notify({
              type: "error",
              title: "Get current user",
              text: error,
              duration: 100000,
          });
      }
    },

    async getAllChannelType () {
      const channelType = await axios.get('/api/channel-type/get-channels');
      let indexCanXoa = channelType.data.data.findIndex(item => item.channel_type_id === 14);
      // Nếu tìm thấy index, xóa bản ghi đó bằng splice
      if (indexCanXoa !== -1) {
        channelType.data.data.splice(indexCanXoa, 1);
      }
      if(channelType.data && channelType.data.data.length) {
        this.listAllChannelType  = channelType.data.data.map(function (o) {
            return {
              value: {
                id: o.channel_type_id,
                objectType: o.object_type,
              },
              label: "(" + o.perfix_object_code +") " + o.name,
            };
          })
          console.log( this.listAllChannelType)
      }
    },
  },
});
