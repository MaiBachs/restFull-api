import { defineStore } from 'pinia';
import axios from "axios"
export const getSearchParent = defineStore("getSearchParentStore", {
    state: () => {
        return {
            getSearchParent: {},
        };
    },
    actions: {
        async actionGetSearchParent () {
            const response = await axios.post('/api/job/fill-check-list-levle-1', {
            });
            if(response.data && response.data.length) {
console.log(response.data)
                this.getSearchParent =  response.data.map(function (o) {
                    return {
                        value: o.job_id,
                        label: o.name,
                    };
                })
            }
        },

    },
});
