import { defineStore } from 'pinia';
import axios from "axios"
export const getCode = defineStore("getCodeStore", {
    state: () => {
        return {
            getCode: {},
        };
    },
    actions: {
        async actionGetCode () {
            const response = await axios.post('/api/job/search-checklist', {
                "code": ""
            });
            if(response.data && response.data.length) {
console.log(response.data)
                this.getCode =  response.data.map(function (o) {
                    return {
                        value: o.code,
                        label: o.code,
                    };
                })
            }
        },

    },
});
