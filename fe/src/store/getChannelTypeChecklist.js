import { defineStore } from 'pinia';
import axios from "axios"
export const getChannelTypeChecklist = defineStore("getChannelTypeChecklistStore", {
    state: () => {
        return {
            listChannelTypeChecklist: {},
        };
    },
    actions: {
        async actionGetChannelTypeChecklist () {
            const channelTypeChecklist = await axios.get('/api/plan/list/fil_channel_type');
            if(channelTypeChecklist.data && channelTypeChecklist.data.length) {
                this.listChannelTypeChecklist  = channelTypeChecklist.data.map(function (o) {
                    return {
                        value: o.channel_type_id,
                        object_type : o.object_type,
                        label: o.name,
                    };
                })
            }
        },
    },
});