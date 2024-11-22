import { createFileRoute } from '@tanstack/react-router'
import {useMemo} from "react";
import {Responsive, WidthProvider} from "react-grid-layout";
import InterfaceChart from "@/components/interface-ui/InterfaceChart.tsx";
import InterfaceNav from "@/components/interface-ui/InterfaceNav.tsx";
import InterfaceOpenOrders from "@/components/interface-ui/InterfaceOpenOrders.tsx";
import InterfaceOrderbook from "@/components/interface-ui/InterfaceOrderbook.tsx";
import InterfaceTrading from "@/components/interface-ui/InterfaceTrading.tsx";
export const Route = createFileRoute('/trade/$id/')({
  component: Interface,
})


function Interface() {
    const ResponsiveGridLayout = useMemo(() => WidthProvider(Responsive), []);
  return (
    <ResponsiveGridLayout
      className="layout overflow-hidden bg-muted"
      breakpoints={{ xxs: 0 }}
      cols={{ xxs: 24 }}
      margin={[2,2]}
      rowHeight={75}
    >
      <div className="bg-card" key="a" style={{zIndex: 10}} data-grid={{ x: 0, y: 0, w: 15, h: 1, static: true }}>
        <InterfaceNav />
      </div>
      <div className="bg-card" key="b" style={{zIndex: 10}} data-grid={{ x: 0, y: 3, w: 15, h: 6, isDraggable: false }}>
        <InterfaceChart />
      </div>
      <div className="bg-card" key="c" style={{zIndex: 10}} data-grid={{ x: 0, y: 13, w: 15, h: 5, isDraggable: false }}>
        <InterfaceOpenOrders />
      </div>
      <div className="bg-card" key="e" style={{zIndex: 10}} data-grid={{ x: 15, y: 0, w: 5, h: 10 }}>
        <InterfaceOrderbook />
      </div>
      <div className="bg-card" key="d" style={{zIndex: 10}} data-grid={{ x: 20, y: 2, w: 4, h: 10 }}>
        <InterfaceTrading />
      </div>
    </ResponsiveGridLayout>
  );
}



