import styles from '../../styles/pages/companyView.module.scss'
import Employee from '../../components/company/employee'
import { useSelector, useDispatch } from 'react-redux'
import { useState } from 'react'

import CompanyController from '../../pages/api/companyController'

export const PagePreloader = (props) => {

    return (
        <div className={styles.preloaderContent}>
            <svg className={styles.door} xmlns="http://www.w3.org/2000/svg" width="177.083" height="250" viewBox="0 0 177.083 250"><g transform="translate(-74.667)"><g transform="translate(74.667)"><path d="M236.125,0H90.292A15.644,15.644,0,0,0,74.667,15.625V244.791A5.21,5.21,0,0,0,79.875,250H246.542a5.21,5.21,0,0,0,5.208-5.208V15.625A15.645,15.645,0,0,0,236.125,0Zm5.208,239.583H85.083V15.625a5.224,5.224,0,0,1,5.208-5.208H236.125a5.224,5.224,0,0,1,5.208,5.208Z" transform="translate(-74.667)"/></g><g transform="translate(106.38 31.714)"><path d="M226.617,42.667H121.7c-2.413,0-4.371,2.223-4.371,4.961V255.992c0,2.738,1.959,4.961,4.371,4.961H226.617c2.413,0,4.372-2.223,4.372-4.961V47.628C230.988,44.889,229.03,42.667,226.617,42.667ZM222.246,251.03h-96.17V52.589h96.17V251.03Z" transform="translate(-117.333 -42.667)"/></g><g transform="translate(167.175 119.065)"><path d="M327.642,256H311.785A23.814,23.814,0,0,0,288,279.785a7.929,7.929,0,1,0,15.857,0,7.952,7.952,0,0,1,7.929-7.929h15.857a7.928,7.928,0,1,0,0-15.857Z" transform="translate(-288 -256)"/></g></g></svg>
            <h2>*Knock knock*</h2>
            <p>Usually they open up in a few seconds.</p>
        </div>
    )
}

export const PageNotFound = (props) => {

    return (
        <div className={styles.preloaderContent}>
            <svg className={styles.panel} xmlns="http://www.w3.org/2000/svg" width="405.344" height="512.001" viewBox="0 0 405.344 512.001"><g transform="translate(-53.328)"><path d="M384,42.667H106.667A10.675,10.675,0,0,0,99.115,45.8L56.448,88.469a10.674,10.674,0,0,0,0,15.083l42.667,42.667a10.744,10.744,0,0,0,7.552,3.115H384a32.039,32.039,0,0,0,32-32V74.667A32.039,32.039,0,0,0,384,42.667Zm10.667,74.666A10.685,10.685,0,0,1,384,128H111.083l-32-32,32-32H384a10.685,10.685,0,0,1,10.667,10.667Z"/><path d="M455.552,237.781l-42.667-42.667A10.744,10.744,0,0,0,405.333,192H128a32.039,32.039,0,0,0-32,32v42.667a32.039,32.039,0,0,0,32,32H405.333a10.675,10.675,0,0,0,7.552-3.136l42.667-42.667A10.673,10.673,0,0,0,455.552,237.781Zm-54.635,39.552H128a10.685,10.685,0,0,1-10.667-10.667V224A10.685,10.685,0,0,1,128,213.333H400.917l32,32Z"/><path d="M256,0a10.671,10.671,0,0,0-10.667,10.667V53.334a10.667,10.667,0,0,0,21.334,0V10.667A10.671,10.671,0,0,0,256,0Z"/><path d="M256,128a10.671,10.671,0,0,0-10.667,10.667v64a10.667,10.667,0,0,0,21.334,0v-64A10.671,10.671,0,0,0,256,128Z"/><path d="M256,277.333A10.671,10.671,0,0,0,245.333,288V501.333a10.667,10.667,0,0,0,21.334,0V288A10.671,10.671,0,0,0,256,277.333Z"/><path d="M320,490.667H192A10.667,10.667,0,0,0,192,512H320a10.667,10.667,0,0,0,0-21.334Z"/></g></svg>
            <h2>Lost way</h2>
            <p>We couldn't find this company. Maybe they moved out?</p>
        </div>
    )
}

const ServiceEdit = (props) => {
    const service = props.service
    const [name, setName] = useState((service && service.name) || "")
    const [duration, setDuration] = useState((service && service.duration) || "")
    const [price, setPrice] = useState((service && service.price) || "")
    const dispatch = useDispatch()
    
    const user = useSelector(state => (state.user))
    const cc = new CompanyController(dispatch)

    const clear = () => {
        setName('')
        setDuration('')
        setPrice('')
    }

    const updateSend = (e) => {
        e.preventDefault()

        let serv = {
            id: (service && service.id) || null,
            name: name,
            duration: duration,
            price: price,
            companyUsername: (props.company && props.company.username) || null
        }

        if(service) {
            cc.updateService(serv, user.token)
            .then(resp => {
                console.log(resp)
                if(resp.status == 'ok') {
                    props.cancel()
                }
            })
            .catch(err => {
                console.log(err)
            })
        } else {
            cc.createService(serv, user.token)
            .then(resp => {
                console.log(resp)
                if(resp.status == "ok") {
                    clear()
                }
            })
            .catch(err => {
                console.log(err)
            })
        }
    }

    return (
        <form className={styles.addForm} onSubmit={e => updateSend(e)}>
            <input value={name} onChange={e => setName(e.target.value)}placeholder="Service name" type="text" name="service_name" />
            <input value={duration} onChange={e => setDuration(e.target.value)}placeholder="Duration (min)" type="text" name="service_duration" />
            <input value={price} onChange={e => setPrice(e.target.value)}placeholder="Price (RON)" type="text" name="service_price" />

            <input type="submit" value="Save" />
            <input onClick={e => props.cancel()} type="button" value="Cancel" />
        </form>
    )
}

const ServiceElement = (props) => {
    const service = props.service
    const user = useSelector(state => (state.user))
    const [editor, setEditor] = useState(false)
    const dispatch = useDispatch()

    const cc = new CompanyController(dispatch)

    service.companyUsername = props.company.username

    const cancel = () => {
        setEditor(false)
    }

    const deleteService = () => {
        cc.deleteService(service, user.token)
    }

    if(editor) {
        return (
            <li key={service.id}>
                <ServiceEdit service={service} cancel={cancel} company={props.company}/>
            </li>
        )
    }

    return (
        <li key={service.id}>
            <div className={styles.serviceName}>{service.name}</div>
            <div>{service.duration} min</div>
            <div>{service.price} RON</div>
            <div>
                <button onClick={e => deleteService()}>Delete</button>
                <button onClick={e => setEditor(true)}>Update</button>
            </div>    
        </li>
    )
}

const PageEdit = (props) => {
    const companyView = useSelector(state => (state.companyView))
    const company = companyView.company

    const [autoSuggest, setAutoSuggest] = useState(false)

    console.log(props.company.services)

    const employeeOptions = [
        {
            id: 0,
            text: 'Remove',
            callBack: console.log
        },
    ]

    return (
        <div className="col-9">
            <div className={styles.board}>
                <h2>Edit services</h2>

                <div className={styles.content}>
                    <ServiceEdit company={company}/>
                </div>

                <ul className={styles.servicesList}>
                    {
                        Array.isArray(company.services) && company.services.map(serv => (
                            <ServiceElement key={serv.id} service={serv} company={company}/>
                        ))
                    }
                </ul>
            </div>

            <div className={styles.board}>
                <h2>Edit employees</h2>

                <div className={styles.employeesEdit}>
                    <form>
                        <div className={styles.formGroup}>
                            <input type='text' name='username' autoComplete="off" placeholder="Add by username" onFocus={e => setAutoSuggest(true)} onBlur={e => setAutoSuggest(false)}/>

                            {autoSuggest && <div className={styles.autoSuggest}>
                                <ul>
                                    <li>Derek</li>
                                    <li>Pha</li>
                                    <li>Phadasdas</li>
                                    <li>Phadasea</li>
                                </ul>
                            </div>}
                        </div>

                        <div className={styles.formGroup}>
                            <select name='service'>
                                {
                                    Array.isArray(company.services) && company.services.map(serv => (
                                        <option key={serv.id} value={serv.name}>{serv.name}</option>
                                    ))
                                }
                            </select>
                        </div>

                        <div className={styles.formGroup}>
                            <input type='submit' value='Add' autoComplete="on"/>
                        </div>
                    </form>

                    <div className={styles.employeesCarousel}>
                        {
                            Array.isArray(company.staffMembers) && company.staffMembers.map(spec => (
                                <Employee key={spec.id} specialist={spec} options={employeeOptions}/>
                            ))
                        }
                    </div>
                </div>
            </div>

            <div className={styles.board}>
                <h2>Edit company info</h2>
            </div>
        </div>
    )
}

const PageContent = (props) => {
    const company = useSelector(state => (state.companyView.company))
    const user = useSelector(state => (state.user))
    const content = props.content

    return (
        <div className="grid">
            <div className="col-3">
                <div className={styles.board}>
                    <h2>Time table</h2>

                    <ul className={styles.timeTable}>
                        <li>Monday</li>
                        <li>Tuesday</li>
                        <li>Wednesday</li>
                        <li>Thursday</li>
                        <li>Friday</li>
                        <li className={styles.closed}>Saturday</li>
                        <li className={styles.closed}>Sunday</li>
                    </ul>
                </div>

                {/* {
                    user.data.id === company.owner.id &&
                    <div className={styles.board}>
                        
                    </div>
                } */}
            </div>

            {
                content === 'main' &&
                <div className="col-9">
                    <div className={styles.board}>
                        <h2>Services</h2>

                        <ul className={styles.servicesList}>
                            {
                                Array.isArray(company.services) && company.services.map(serv => (
                                <li key={serv.id}><div className={styles.serviceName}>{serv.name}</div><div>{serv.duration} min</div><div>{serv.price} RON</div></li>
                                ))
                            }
                        </ul>
                    </div>

                    <div className={styles.board}>
                        <h2>Staff</h2>

                        <div className={styles.employeesCarousel}>
                            {
                                Array.isArray(company.staffMembers) && company.staffMembers.map(spec => (
                                    <Employee key={spec.id} specialist={spec}/>
                                ))
                            }
                        </div>
                    </div>
                </div>
            }
            {
                content === 'manage' &&
                <PageEdit company={company}/>
            }
        </div>
    )
}

export default PageContent