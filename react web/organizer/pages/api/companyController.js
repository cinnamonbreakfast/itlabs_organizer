import axios from 'axios'

class CompanyController {
    constructor(dispatcher) {
        this.dispatcher = dispatcher
    }

    getCompany(identifier) {
        return axios.get(process.env.REQ_HOST + '/c/' + identifier)
    }
}

export default CompanyController