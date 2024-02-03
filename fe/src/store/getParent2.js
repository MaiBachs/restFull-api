import { defineStore } from 'pinia';
import axios from "axios"
// import {reactive} from "vue";
export const getParent2 = defineStore("getParent2Store", {
    state: () => {
        return {
            listParent2: {},
        };
    },
    actions: {
        async actionGetParent2 (parentId) {
            const Parent2 = await axios.post('/api/job/fill-check-list-by-parentId',
                {
                    parent_id: parentId
                });
            if(Parent2.data && Parent2.data.length) {
                this.listParent2  = Parent2.data.map(function (o) {
                    return {
                        value: o.job_id,
                        label: o.code,
                    };
                })
                console.log("ok")
                console.log(this.listParent2)
            }
        },
    },
});