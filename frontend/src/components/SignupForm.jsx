export default function SignupForm(props){
    const hasSubmittedAny = props.submitStatus.login || props.submitStatus.signup

    return (
    <>
        <div className="label-input-container">
            <label htmlFor="username">Username</label>
            <input 
                id="username" 
                name="username" 
                type="text" 
                value={props.signupData.username} 
                onChange={props.handleChange}
            />
        </div>
        <div className="label-input-container">
            <label htmlFor="email">Email</label>
            <input 
                id="email" 
                name="email" 
                type="text" 
                value={props.signupData.email} 
                onChange={props.handleChange}
            />
        </div>
        <div className="label-input-container">
            <label htmlFor="password">Password</label>
            <input 
                id="password" 
                name="password" 
                type="text" 
                value={props.signupData.password} 
                onChange={props.handleChange}
            />
        </div>
        <div className="label-input-container">
            <label htmlFor="password">Confirm Password</label>
            <input 
                id="confirmPassword" 
                name="confirmPassword" 
                type="text" 
                value={props.signupData.confirmPassword} 
                onChange={props.handleChange}
            />
        </div>
        <button onClick={props.submitForm} disabled={hasSubmittedAny}>
            {props.submitStatus.signup?
                "Signing up..." :
                "Sign Up"
            }
        </button>
    </>
    )
}