import {defineStore} from 'pinia';
import axios from "axios"

export const getName = defineStore("getNameStore", {
    state: () => {
        return {
            getName: {},
        };
    },
    actions: {
        async actionGetName() {
            const response = await axios.post('/api/job/search-checklist', {
                "name": ""
            });
            if (response.data && response.data.length) {
                this.getName = response.data.map(function (o) {
                    return {
                        value: o.name,
                        label: o.name,
                    };
                })
            }
        },

    },
});
