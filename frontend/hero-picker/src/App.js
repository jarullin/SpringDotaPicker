import './App.css';
import { useState, useEffect } from 'react';
import Qs from 'query-string';
import axios from 'axios';
import HeroPicker from './components/HeroPicker';
import Recommendations from './components/Recommendations';
function App() {
  const [heroes, setHeroes] = useState([]);
  const [selectedEnemies, setSelectedEnemies] = useState([]);
  const [recommendations, setRecommendations] = useState([]);

  useEffect(() => {
    console.log("Trying to connect");

    axios.get('http://localhost:8080/api/heroes')
      .then(response => setHeroes(response.data))
      .catch(error => console.error('Error fetching heroes:', error));

  }, []);

  const handleEnemyDeselect = (heroId) => {
    console.log("FIRE: handleEnemyDeselect");
    setSelectedEnemies(selectedEnemies.filter(hero => hero.id !== heroId));
  }

  const handleEnemySelect = (heroId) => {
    console.log("FIRE: handleEnemySelect");
    if (selectedEnemies.length < 5) {
      setSelectedEnemies([...selectedEnemies, heroes.find(hero => hero.id === heroId)]);
    }
  };

  const getRecommendations = () => {
    const map1 = selectedEnemies.map(e => e.id)
    console.log(map1);

    axios.get('http://localhost:8080/api/picks',
      {
        params: { enemies: map1 },
        paramsSerializer: (params) => Qs.stringify(params, { arrayFormat: 'repeat' })
      }
    )
      .then(response => setRecommendations(response.data))
      .catch(error => console.error('Error fetching recommendations:', error));
    console.log("recommendations");
    console.log(recommendations);

  };

  return (
    <div className="App">
      <h1>Hero Picker</h1>
      <button
        onClick={getRecommendations}
        disabled={selectedEnemies.length === 0}
      >
        Get Recommendations
      </button>
      <HeroPicker
        heroes={heroes}
        onEnemySelect={handleEnemySelect}
        onEnemyDeselect={handleEnemyDeselect}
        selectedEnemies={selectedEnemies}
      />
      {recommendations && <Recommendations recommendations={recommendations} />}
    </div>
  );
}

export default App;
