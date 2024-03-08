import React, {useState} from "react";
import {useStore} from "../store/store";
import {useNavigate} from "react-router-dom"; // Import useNavigate

interface LoginFormProps {
}

const LoginForm: React.FC<LoginFormProps> = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [passwordConfirmation, setPasswordConfirmation] = useState("");
    const [passwordConfirmationError, setPasswordConfirmationError] = useState(false);


    const resetPasswordAction = useStore((state) => state.resetPassword);
    const navigate = useNavigate();

    const handleResetPassword = async (e: React.FormEvent) => {
        //zajistuje nam aby se po submit nerefreshla stranka
        e.preventDefault();
        if (password == passwordConfirmation) {
            setPasswordConfirmationError(false)
            await resetPasswordAction(email, password);
            navigate("/login");
            window.location.reload();
        } else {
            setPasswordConfirmationError(true)
        }
    };

    return (
        <div className="resetForm">
            <h1 style={{paddingBottom: "20px"}}>Obnova hesla</h1>
            <form onSubmit={handleResetPassword}>
                <div className="mb-3">
                    <label htmlFor="email" className="form-label">
                        E-mail
                    </label>
                    <input
                        type="email"
                        className="form-control"
                        id="email"
                        aria-describedby="emailHelp"
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="password" className="form-label">
                        Nové heslo
                    </label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="password" className="form-label">
                        Potvrzení hesla
                    </label>
                    <input
                        type="password"
                        className="form-control"
                        id="passwordConfirmation"
                        onChange={(e) => setPasswordConfirmation(e.target.value)}
                        required
                    />
                </div>
                {passwordConfirmationError &&
                    <p>
                        Hesla nejsou stejná
                    </p>
                }
                <div className="d-grid">
                    <button type="submit" className="btn btn-dark btn-lg">
                        Obnovit
                    </button>
                    <div className="text-center">
                    </div>
                </div>
            </form>
        </div>
    );
};

export default LoginForm;
