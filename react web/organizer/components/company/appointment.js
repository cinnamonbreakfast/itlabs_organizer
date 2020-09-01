import styles from '../../styles/pages/companyView.module.scss'
import Calendar from 'react-calendar'
import { useState } from 'react'
import { useSelector } from 'react-redux'

import axios from 'axios'

const monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
    ];

const Step3 = (props) => {
    const day = props.day

    return (
        <div className={styles.hourPick}>
            <p>Pick an hour for {day.getDate() +' '+ monthNames[day.getMonth()]}</p>
            <input type="time" value={props.hour} onChange={e => props.select(e.target.value || (new Date()).getHours())}/>
        </div>
    )
}

const Step2 = (props) => {
    const services = props.services
    const day = props.day

    const select = (e) => {
        props.select(e.target.value)
    }


    return (
        <div className={styles.servicePick}>
            <p>Pick some service available for {day.getDate() +' '+ monthNames[day.getMonth()]}</p>
            <select name="service" onChange={select} placeholder="Select a service">
            <option value="" key={-1} disabled selected>Select your option</option>
            {
                Array.isArray(services) && services.map(s => {
                    if(s.specialistDTOList.length > 0) {
                        return s.specialistDTOList.map(sp => (
                        <option key={s.id} value={sp.id}>{s.name} at {sp.user.name}</option>
                        ))
                    } else return null
                })
            }
            </select>
        </div>
    )
}

const AppointPop = (props) => {
    const user = useSelector(state => (state.user))

    const [step, setStep] = useState(0)
    const company = props.company
    const [payload,setPayload] = useState(null)
    const [date, setDate] = useState(Date.now())
    const [service, setService] = useState(null) // de fapt e angajat
    const [hour, setHour] = useState(null) // de fapt e angajat

    const appointementRequest = async ()=>{
        return new Promise((resolve,rej)=>{
            let sp_id = service
            let duration
            let d = company.services
            for(let i in d){
            
                let b = d[i].specialistDTOList
                for(let j  in d[i].specialistDTOList)
                {
                    
                    if(sp_id == b[j].id){
                        duration = d[i].duration
                        i=d.length
                        break;
                    }
                }
            }
            let arr = hour.split(':')
            let hh = parseInt(arr[0])
            let mm = parseInt( arr[1])
            console.log(arr,newDate)
            let newDate = new Date( date.toString())
            console.log(arr,newDate)
            newDate.setMinutes(newDate.getMinutes()+mm)
            console.log(arr,newDate,hh)
            newDate.setHours(newDate.getHours()+hh+3)
          
             
             console.log(newDate.toISOString())
            const url =process.env.REQ_HOST+'/schedule/create'
            axios.post(url,null,{headers:{
                'token':user.token
            },params:{
                id:0,
                duration:duration*360,
                specialistId:service,
                start:newDate.toISOString(),
                end:null,
                s_start:null,
                s_end:null,
                specialistDTO:null
            }}).then(resp=>{
                resolve(resp.data)
            }).catch(e=>{
                resolve(e.response.data)
            })
        }).then(e=>{
            setPayload(e)
        });
    }

    const change = (e) => {
        setDate(e)
        setStep(1)
    }

    const back = (e) => {
        if(step > 0) {
            setStep(step - 1)}
        else props.close()
    }

    const next = (e) => {
        // la step 0 nu am facut next ca nu apare next pe calendar
        if(step === 3){ props.close()
          
        
        }

        if(step === 1) {
            if(!service) {
                alert("Please, select a service")
            } else {
                setStep(step + 1)
            }
        } else if(step === 2) {
            console.log(hour)
            appointementRequest()
            // la al doilea pas verific daca e pusa ora
            // poti da aici display si daca e available
            if(!hour) {
                alert("Please, select an hour!")
            } else {
                setStep(step + 1)
            }
        } else {
            setStep(step + 1)
        }
    }



    return (
        <div className={styles.popup}>
            <h2>New appointment</h2>
            <p>Pick up some info to set up your appointment.</p>

            { step === 0 && <Calendar className={styles.calendar + ' appCalendar'} onChange={e => change(e)} tileDisabled={({activeStartDate, date, view }) => date < (Date.now() - (24*60*60*1000) * 1)}/> }
            { step === 1 && <Step2 services={company.services} day={date} select={setService}/>}
            { step === 2 && <Step3 services={company.services} day={date} hour={hour} select={setHour}/>}
            { step === 3 &&payload&&
            <div className={styles.final}>
                <h2>{payload}</h2>

            </div>
            }

            <div className={styles.controlls}>
                {step!=3&&<button onClick={e => back(e)}>{step === 0 && 'Cancel' || 'Back'}</button>}
                {step >= 1 && <button onClick={e => next()}>{step === 3 && 'Close' || 'Next'}</button>}
            </div>
        </div>
    )
}

export default AppointPop