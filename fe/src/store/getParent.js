import { defineStore } from 'pinia';
import axios from "axios"
export const getParent = defineStore("getParentStore", {
    state: () => {
        return {
            listParent: {},
        };
    },
    actions: {
        async actionGetParent () {
            const Parent = await axios.post('/api/job/fill-check-list-levle-1');
            if(Parent.data && Parent.data.length) {
                this.listParent  = Parent.data.map(function (o) {
                    return {
                        value: o.job_id,
                        label: o.name,
                    };
                })
            }
        },
    },
});