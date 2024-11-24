import {Link, useRouter} from "@tanstack/react-router";
import {ThemeToggle} from "./theme-toggle.tsx";
import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuLink, NavigationMenuList,
    navigationMenuTriggerStyle
} from "../ui/navigation-menu.tsx";
import {useAuth} from "@/auth.tsx";
import {Route} from "@/routes/_auth.tsx";
import { Button } from "../ui/button.tsx";

export default function Navbar() {
    const router = useRouter()
    const navigate = Route.useNavigate()
    const auth = useAuth()

    const handleLogout = () => {
        if (window.confirm('Are you sure you want to logout?')) {
            auth.logout().then(() => {
                router.invalidate().finally(() => {
                    navigate({ to: '/' })
                })
            })
        }
    }
    return (
        <header className="p-2 px-6 bg-background flex justify-between items-center grow-0 shrink basis-[64px]">
            <div>
                <NavigationMenu>
                    <NavigationMenuList>
                        <NavigationMenuItem>
                            <NavigationMenuLink className={navigationMenuTriggerStyle()} asChild>
                                <Link to="/">Home</Link>
                            </NavigationMenuLink>
                        </NavigationMenuItem>
                        <NavigationMenuItem>
                            <NavigationMenuLink className={navigationMenuTriggerStyle()} asChild>
                                <Link to="/trade">trade</Link>
                            </NavigationMenuLink>
                        </NavigationMenuItem>
                        <NavigationMenuItem>
                            <NavigationMenuLink className={navigationMenuTriggerStyle()} asChild>
                                <Link to="/dashboard">dashboard</Link>
                            </NavigationMenuLink>
                        </NavigationMenuItem>
                        <NavigationMenuItem>
                            <NavigationMenuLink className={navigationMenuTriggerStyle()} asChild>
                                <Link to="/trade/hello">trade/hello</Link>
                            </NavigationMenuLink>
                        </NavigationMenuItem>
                        <NavigationMenuItem>
                            <NavigationMenuLink className={navigationMenuTriggerStyle()} asChild>
                                <Link to="/trade/start">trade/start</Link>
                            </NavigationMenuLink>
                        </NavigationMenuItem>
                    </NavigationMenuList>
                </NavigationMenu>
            </div>
            <div className="flex gap-2 items-center">
                {auth.isAuthenticated ? (
                    <Button onClick={() => handleLogout()}>Logout</Button>
                ) : (
                    <Button onClick={() => navigate({ to: '/login'})}>Login</Button>
                )}
                <ThemeToggle/>
            </div>
        </header>
    )
}