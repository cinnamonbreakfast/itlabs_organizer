import styles from '../../styles/pages/companyView.module.scss'
import Calendar from 'react-calendar'
import { useState } from 'react'

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
        console.log(e)

        props.select(e.target.value)
    }

    return (
        <div className={styles.servicePick}>
            <p>Pick some service available for {day.getDate() +' '+ monthNames[day.getMonth()]}</p>
            <select name="service" onChange={e => select(e)} placeholder="Select a service">
            <option value="" key={-1} disabled selected>Select your option</option>
            {
                Array.isArray(services) && services.map(s => {
                    if(s.specialistDTOList.length > 0) {
                        return s.specialistDTOList.map(sp => (
                        <option key={s.id} value={sp.user.name}>{s.name} at {sp.user.name}</option>
                        ))
                    } else return null
                })
            }
            </select>
        </div>
    )
}

const AppointPop = (props) => {
    const [step, setStep] = useState(0)
    const company = props.company

    const [date, setDate] = useState(Date.now())
    const [service, setService] = useState(null) // de fapt e angajat
    const [hour, setHour] = useState(null) // de fapt e angajat

    const change = (e) => {
        setDate(e)
        setStep(1)
    }

    const back = (e) => {
        if(step > 0) setStep(step - 1)
        else props.close()
    }

    const next = (e) => {
        // la step 0 nu am facut next ca nu apare next pe calendar
        if(step === 3) props.close()

        if(step === 1) {
            if(!service) {
                alert("Please, select a service")
            } else {
                setStep(step + 1)
            }
        } else if(step === 2) {
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

    console.log(step, Date.now(), service)

    return (
        <div className={styles.popup}>
            <h2>New appointment</h2>
            <p>Pick up some info to set up your appointment.</p>

            { step === 0 && <Calendar className={styles.calendar + ' appCalendar'} onChange={e => change(e)} tileDisabled={({activeStartDate, date, view }) => date < (Date.now() - (24*60*60*1000) * 1)}/> }
            { step === 1 && <Step2 services={company.services} day={date} select={setService}/>}
            { step === 2 && <Step3 services={company.services} day={date} hour={hour} select={setHour}/>}
            { step === 3 &&
            <div className={styles.final}>
                <h1>Your appointment is set!</h1>
                <p>You can cancel it anytime in your profile tab.</p>    
            </div>}

            <div className={styles.controlls}>
                <button onClick={e => back(e)}>{step === 0 && 'Cancel' || 'Back'}</button>
                {step >= 1 && <button onClick={e => next()}>{step === 3 && 'Close' || 'Next'}</button>}
            </div>
        </div>
    )
}

export default AppointPop