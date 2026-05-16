#!/bin/bash
echo "Starting Smart Career System Frontends..."
echo ""
echo "User Frontend: http://localhost:3000"
echo "Company Frontend: http://localhost:3001"
echo "Admin Frontend: http://localhost:3002"
echo ""

# Start user frontend
npm run dev:user &
PID1=$!

# Start company frontend
npm run dev:company &
PID2=$!

# Start admin frontend
npm run dev:admin &
PID3=$!

echo "All frontends started!"
echo "User: http://localhost:3000 (PID: $PID1)"
echo "Company: http://localhost:3001 (PID: $PID2)"
echo "Admin: http://localhost:3002 (PID: $PID3)"

# Wait for all processes
wait
