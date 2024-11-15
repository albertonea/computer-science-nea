import {Link} from "@tanstack/react-router";
import {ThemeToggle} from "./theme-toggle.tsx";
import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuLink, NavigationMenuList,
    navigationMenuTriggerStyle
} from "../ui/navigation-menu.tsx";

export default function Navbar() {
    return (
        <header className="p-2 px-6 bg-background flex justify-between items-center h-[64px]">
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
            <div></div>
            <ThemeToggle/>
        </header>
    )
}