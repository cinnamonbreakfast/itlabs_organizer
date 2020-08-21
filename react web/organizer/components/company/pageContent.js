import styles from '../../styles/pages/companyView.module.scss'
import Employee from '../../components/company/employee'

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

const PageContent = (props) => {
    const company = props.company

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
            </div>

            <div className="col-9">
                <div className={styles.board}>
                    <h2>Services</h2>

                    <ul className={styles.servicesList}>
                        <li>
                            <div className={styles.serviceName}>Tuns</div>
                            <div>30 min</div>
                            <div>10$</div>
                        </li>
                    </ul>
                </div>

                <div className={styles.board}>
                    <h2>Staff</h2>

                    <div className={styles.employeesCarousel}>
                        <Employee/>
                        <Employee/>
                        <Employee/>
                        <Employee/>
                        <Employee/>
                        <Employee/>
                        <Employee/>
                        <Employee/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default PageContent