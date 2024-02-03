import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore'

export const getStaffOwner = defineStore("getOwnerStaff", {
  state: () => {
    return {
      listStaffOwner: {},
      listAllStaffOwner: {},
      listDetailsOwner: {},
      listChannelCode: {},
      listAllChannelCode: {},
      listDetailsChannelCode : {},
      loaddingStore : useLoaddingStore(),
    };
  },
  actions: { 
    async actionGetStaffOwner (shopId) {
      this.loaddingStore.loading = true;
      if(shopId){
        const ownerStaffList = await axios.post(`/api/employees/search`,{
          shop_id: shopId,
        });
        console.log(ownerStaffList.data)
        if(ownerStaffList.data && ownerStaffList.data.data.length) {
          console.log(ownerStaffList.data)
          this.listStaffOwner  = ownerStaffList.data.data.map(function (o) {
              return {
                value: o.id ? o.id : o.shop_id,
                label: o.code ? o.code : o.shop_code
              };
            })
            this.listAllStaffOwner = ownerStaffList.data.data
        }else{
          this.listStaffOwner = []
          this.listAllStaffOwner = []
          this.listDetailsOwner = []
        }
      }else{
        this.listStaffOwner = []
      }
      this.loaddingStore.loading = false;

    },

    async actionGetChanel (formData) {
      this.loaddingStore.loading = true;
      if(formData.branch_id && formData.channel_type_id){
        const reponse = await axios.post(`/api/Location-manager/get-channelCode`,{
          shop_id : formData.business_id ? formData.business_id : formData.branch_id,
          object_type : formData.channel_type_id.objectType,
          channel_type_id : formData.channel_type_id.id,
          owner_id: formData.owner_id
        });
        console.log(reponse.data)
        if(reponse.data && reponse.data.length) {
          this.listChannelCode  = reponse.data.map(function (o) {
              return {
                value: o.id ? o.id : o.shop_id,
                label: o.code ? o.code : o.shop_code
              };
            })
          this.listAllChannelCode = reponse.data
        }else{
          this.listChannelCode = []
          this.listAllChannelCode = []
          this.listDetailsChannelCode = []
        }
        console.log(this.listAllChannelCode)
      }else{
        this.listChannelCode = []
      }
      this.loaddingStore.loading = false;

    },
  },
});
