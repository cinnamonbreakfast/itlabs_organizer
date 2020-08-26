import { useRouter } from 'next/router'
import { useState, useEffect } from 'react'
import { Calendar, momentLocalizer } from 'react-big-calendar'
import moment from 'moment'
import styles from '../../styles/pages/calendar.module.scss'

const Schedule = (req, res) => {
    const router = useRouter()
    const localizer = momentLocalizer(moment)

    const programari = [
        {
            title: 'Masaj',
            allDay: false,
            start: new Date('August 25, 2020 14:00:00'),
            end: new Date('August 25, 2020 14:30:00'),
        },
        {
            title: 'Masaj',
            allDay: false,
            start: new Date('August 25, 2020 14:30:00'),
            end: new Date('August 25, 2020 15:00:00'),
        },
        
    ]

    console.log(router.query)

    useEffect(() => {

    }, [router])

    return (
        <div className={styles.calendarWrapper}>
            <Calendar
                localizer={localizer}
                events={programari}
                startAccessor="start"
                endAccessor="end"
            />
        </div>
    )
}

export default Schedule