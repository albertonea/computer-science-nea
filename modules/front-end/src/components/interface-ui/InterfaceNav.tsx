import {Separator} from "../ui/separator.tsx";
import {ScrollArea, ScrollBar} from "../ui/scroll-area.tsx";
import {Route} from "@/routes/_auth/trade/$ticker";

export default function InterfaceNav() {
    const {ticker} = Route.useParams()

    return (
      <div className="py-2 px-4 flex h-full items-center">
        <div className="gap-4 flex h-full items-center pr-8">
          <div className="text-xl">{ticker}/USD</div>
          <Separator orientation="vertical" />
          <div className="text-lg text-positive">68,372.56</div>
        </div>
        <ScrollArea type="hover">
            <div className="flex items-center gap-4 flex-nowrap text-nowrap">
                <div className="flex flex-col gap-1 text-xs">
                    <span className="text-muted-foreground">24h Change</span>
                    <div className="flex items-center gap-1 text-positive">
                        <span className="">100.32</span>
                        <span className="">+0.6%</span>
                    </div>
                </div>
                <div className="flex flex-col gap-1 text-xs">
                    <span className="text-muted-foreground">24h High</span>
                    <span className="">68,0456.43</span>
                </div>
                <div className="flex flex-col gap-1 text-xs">
                    <span className="text-muted-foreground">24h Low</span>
                    <span className="">67,387.54</span>
                </div>
                <div className="flex flex-col gap-1 text-xs">
                    <span className="text-muted-foreground">24h Volume(BTC)</span>
                    <span className="">19,000</span>
                </div>
                <div className="flex flex-col gap-1 text-xs">
                    <span className="text-muted-foreground">24h Volume(USD)</span>
                    <span className="">1,439,039,362.83</span>
                </div>
            </div>
            <ScrollBar orientation="horizontal"></ScrollBar>
        </ScrollArea>
      </div>
    );
}
