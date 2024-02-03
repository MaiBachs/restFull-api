import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore';

export const getJobsToPlan = defineStore("getJobsToPlanStore", {
  state: () => {
    return {
        getJobsToPlan: {},
    };
  },
  actions: { 
    async actionGetJobsToPlan () {
        const jodPlan = await axios.get('/api/evaluation-config/get-evaluation-audit');
        if(jodPlan.data && jodPlan.data.length) {
            this.getJobsToPlan  = jodPlan.data.map(function (o) {
                return {
                  value:  o.job_id,
                   
                  label: o.code,
                };
              })
          }
      }, 

      async selectBoxGetJobsToPlan () {
        const loaddingStore = useLoaddingStore();
        loaddingStore.loading = true;
        const jodPlan = await axios.get('/api/evaluation-config/get-evaluation-audit');
        if(jodPlan.data && jodPlan.data.length) {
            this.getJobsToPlan  = jodPlan.data.map(function (o) {
                return {
                  value: o.job_id,
                  label: o.code,
                };
              })
          }
        loaddingStore.loading = false;
      }, 
  },
});
