import axios from 'axios'
import { company_view_actions as cva } from './redux/companyViewer'

class CompanyController {
    constructor(dispatcher) {
        this.dispatcher = dispatcher
    }

    async getCompany(identifier) {
        return axios.get(process.env.REQ_HOST + '/c/' + identifier)
        .then(resp => {
            if(resp.data) {
                return resp.data
            }
            return null;
        })
        .catch(err => {
            console.log(err)
            if(err.response && err.response.data) return {message: err.response.data, code: 404 }
            return {message: "unknown error", code: 404 }
        })
    }

    dispatchCompany(id) {
        this.getCompany(id).then(r => {
            if(r && this.dispatcher && !r.code) {
                this.dispatcher({type: cva.SET_VIEW_COMPANY, payload: r})
                console.log('set up comp')
            } else {
                console.log('erer1')
                this.dispatcher({type: cva.CLEAR_VIEW_COMPANY})
                this.dispatcher({type: cva.SET_FIND_CODE, payload: 404})
            }
        })
        .catch(e => {
            console.log(e)
            if(this.dispatcher) {
                this.dispatcher({type: cva.SET_FIND_CODE, payload: 404})
                this.dispatcher({type: cva.CLEAR_VIEW_COMPANY})
            }
        })
    }

    async updateService(service, token) {
        console.log(service, token)
        return axios({
            url: process.env.REQ_HOST + '/service/update',
            method: 'PUT',
            data: service,
            headers: {
                'TOKEN': token,
            },
        }).then(_resp => {
            console.log(_resp)
            this.dispatchCompany(service.companyUsername)
            return {status: 'ok', message: 'Service was added!'}
        }).catch(_err => {
            console.log(_err)
            console.log(_err.response)
            return {status: 'bad', message: (_err.response || _err.response) || "Unknown error occured. Try again."}
        })
    }

    async deleteService(service, token) {
        return axios({
            url: process.env.REQ_HOST + '/service/delete',
            method: 'DELETE',
            data: service,
            headers: {
                'TOKEN': token,
            },
        }).then(_resp => {
            console.log(_resp)
            console.log(service.companyUsername)
            service.companyUsername && this.dispatchCompany(service.companyUsername)
            return {status: 'ok', message: 'Service was deleted!'}
        }).catch(_err => {
            console.log(_err)
            console.log(_err.response)
            console.log(service.companyUsername)
            console.log(service)
            return {status: 'bad', message: (_err.response || _err.response) || "Unknown error occured. Try again."}
        })
    }

    async createService(service, token) {
        console.log(service, token)
        return axios({
            url: process.env.REQ_HOST + '/service/create',
            method: 'POST',
            data: service,
            headers: {
                'TOKEN': token,
            },
        }).then(_resp => {
            console.log(_resp)
            this.dispatchCompany(service.companyUsername)
            return {status: 'ok', message: 'Service was added!'}
        }).catch(_err => {
            console.log(_err)
            console.log(_err.response)
            return {status: 'bad', message: (_err.response || _err.response) || "Unknown error occured. Try again."}
        })
    }
}

export default CompanyController