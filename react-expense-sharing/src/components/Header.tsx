import React from "react";
import {Button, Dropdown} from "react-bootstrap";
import "../css/Header.css"; // Předpokládá se soubor s CSS
import logoPng from "../img/logo_desolve.png";
import {useStore} from "../store/store";
import {useNavigate, Link} from "react-router-dom"; // Import useNavigate

interface HeaderProps {
    // zde můžete přidat vlastní props, pokud jsou potřeba
}

const Header: React.FC<HeaderProps> = () => {
    const {isAuthenticated, logoutAction} = useStore((state) => ({
        isAuthenticated: state.isAuthenticated,
        logoutAction: state.logout,
    }));
    const navigate = useNavigate();

    const handleLogout = async () => {
        console.log("Log out");
        logoutAction();
        navigateTo("/login");
        window.location.reload();
    };

    const navigateTo = (path: string) => {
        navigate(path);
    };

    return (
        <header className="header">
            {/* Your logo and other elements */}
            {/* Bootstrap Dropdown for Mobile */}
            <div className="d-block d-md-none" style={{textAlign: "center"}}>
                <h1>DEBT SOLVER</h1>
            </div>
            <div className="d-block d-md-none">
                <Dropdown>
                    <Dropdown.Toggle variant="secondary" id="dropdown-basic">
                        Menu
                    </Dropdown.Toggle>

                    <Dropdown.Menu>
                        {isAuthenticated ? (
                            <>
                                <Dropdown.Item onClick={() => navigateTo("/dashboard")}>
                                    Domů
                                </Dropdown.Item>
                                <Dropdown.Item onClick={() => navigateTo("/friends")}>
                                    Přátelé
                                </Dropdown.Item>
                                <Dropdown.Item onClick={() => navigateTo("/receivables")}>
                                    Dluží mě
                                </Dropdown.Item>
                                <Dropdown.Item onClick={() => navigateTo("/debts")}>
                                    Dlužím
                                </Dropdown.Item>
                                <Dropdown.Item onClick={() => navigateTo("/groups")}>
                                    Skupiny
                                </Dropdown.Item>
                                <Dropdown.Item onClick={() => navigateTo("/history")}>
                                    Historie
                                </Dropdown.Item>
                                <Dropdown.Item onClick={handleLogout}>
                                    Odhlásit
                                </Dropdown.Item>
                                {/* Other authenticated links */}
                            </>
                        ) : (
                            <>
                                <Dropdown.Item onClick={() => navigateTo("/home")}>
                                    Domů
                                </Dropdown.Item>
                                <Dropdown.Item onClick={() => navigateTo("/login")}>
                                    Přihlášení
                                </Dropdown.Item>
                                <Dropdown.Item onClick={() => navigateTo("/register")}>
                                    Registrace
                                </Dropdown.Item>
                                {/* Other unauthenticated links */}
                            </>
                        )}
                    </Dropdown.Menu>
                </Dropdown>
            </div>

            {/* Regular menu for larger screens */}
            <div className="d-none d-md-flex justify-content-between align-items-center">
                <img src={logoPng} alt="Desolve logo" style={{width: "50px"}}/>
                {isAuthenticated ? (
                    <>
                        <Button
                            onClick={() => navigateTo("/dashboard")}
                            variant="secondary"
                            className="btn-header"
                        >
                            Domů
                        </Button>
                        <Button
                            onClick={() => navigateTo("/friends")}
                            variant="secondary"
                            className="btn-header"
                        >
                            Přátelé
                        </Button>
                        <Button
                            onClick={() => navigateTo("/receivables")}
                            variant="secondary"
                            className="btn-header"
                        >
                            Dluží mě
                        </Button>
                        <Button
                            onClick={() => navigateTo("/debts")}
                            variant="secondary"
                            className="btn-header"
                        >
                            Dlužím
                        </Button>
                        <Button
                            onClick={() => navigateTo("/groups")}
                            variant="secondary"
                            className="btn-header"
                        >
                            Skupiny
                        </Button>
                        <Button
                            onClick={() => navigateTo("/history")}
                            variant="secondary"
                            className="btn-header"
                        >
                            History
                        </Button>
                    </>
                ) : (
                    <>
                        <Button
                            onClick={() => navigateTo("/home")}
                            variant="secondary"
                            className="btn-header"
                        >
                            Domů
                        </Button>
                    </>
                )}
            </div>
            <div className="d-none d-md-block">
                {isAuthenticated ? (
                    <>
                        <Button
                            variant="secondary"
                            className="btn-header"
                            onClick={handleLogout}
                        >
                            Odhlásit
                        </Button>
                    </>
                ) : (
                    <>
                        <Button
                            variant="secondary"
                            className="btn-header"
                            onClick={() => navigateTo("/register")}
                        >
                            Registrace
                        </Button>
                        <Button
                            variant="secondary"
                            className="btn-header"
                            onClick={() => navigateTo("/login")}
                        >
                            Přihlásit se
                        </Button>
                    </>
                )}
            </div>
        </header>
    );
};

export default Header;
