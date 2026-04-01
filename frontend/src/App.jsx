import TradingScreen from "./pages/TradingScreen";

function Footer() {
  return (
    <footer
      style={{
        borderTop: "1px solid #eee",
        padding: "8px 16px",
        fontSize: "12px",
        color: "#eee",
        textAlign: "center",
        backgroundColor: "#050816",
      }}
    >
      © {new Date().getFullYear()} CJ Trading Desk
    </footer>
  );
}

function App() {
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        height: "100vh", // 화면 높이 딱 맞게
      }}
    >
      <TradingScreen />
      <Footer />
    </div>
  );
}

export default App;
