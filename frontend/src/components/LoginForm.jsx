export default function LoginForm(props){
    const hasSubmittedAny = props.submitStatus.login || props.submitStatus.signup

    return (
    <>
        <div className="label-input-container">
            <label htmlFor="usernameEmail">Username/Email</label>
            <input 
                id="usernameEmail"
                name="usernameEmail"
                type="text" 
                value={props.loginData.usernameEmail} 
                onChange={props.handleChange}
            />
        </div>
        <div className="label-input-container">
            <label htmlFor="password">Password</label>
            <input 
                id="password" 
                name="password"
                type="text" 
                value={props.loginData.password} 
                onChange={props.handleChange}
            />
        </div>
        <button onClick={props.submitForm} disabled={hasSubmittedAny}>
            {props.submitStatus.login? 
                "Logging in..." : 
                "Log In"
            }
        </button>
    </>
    )
}