import { act, use, useEffect, useReducer, useState } from 'react'
import './App.css'
import axios, { all } from 'axios'
import Qs from 'query-string';
import { Recommendation } from './components/Recommendation';
import { Hero } from './components/Hero';
import { TextField } from '@mui/material';

const initialState = {
  allies: [],
  enemies: [],
  bans: [],
  blocked: new Set(),
}
function reducer(state, action) {
  console.log(action);

  switch (action.type) {
    case "SELECT_HERO": {
      const { heroId, target } = action;

      const otherLists = ["allies", "bans", "enemies"].filter(listName => listName !== target);

      let updatedState = { ...state, blocked: new Set(state.blocked).add(heroId) };

      otherLists.forEach(listName => {
        updatedState[listName] = updatedState[listName].filter(h => h !== heroId);
      });

      return {
        ...updatedState,
        [target]: [...updatedState[target], heroId],
      };
    }

    case "REMOVE_HERO": {
      const { heroId, target } = action;
      //remove from target
      const newList = state[target].filter(h => h !== heroId);
      const newBlocked = new Set(state.blocked);
      newBlocked.delete(heroId);

      return {
        ...state,
        [target]: newList,
        blocked: newBlocked,
      };
    }

    default:
      return state;
  }
}




function App() {
  const [heroes, setHeroes] = useState<{ id: number, imageLink: string, name: string }[]>([])
  const [recommendations, setRecommendations] = useState<{ carry: number[] }[]>([]);
  const [filter, setFilter] = useState('');
  const [state, dispatch] = useReducer(reducer, initialState)

  useEffect(() => {
    axios.get('http://localhost:8080/api/heroes')
      .then(response => {
        setHeroes(response.data);
      })
      .catch(error => console.error('Error fetching heroes:', error));
  }, []);


  const handleHeroClick = (heroId: number, button: number) => {
    setFilter('');
    if (button === 0) {
      // Left click
      if (state.allies.includes(heroId)) {
        dispatch({ type: "REMOVE_HERO", heroId: heroId, target: "allies" });
      } else {
        if (state.allies.length < 5)
          dispatch({ type: "SELECT_HERO", heroId: heroId, target: "allies" });
      }
    }
    else if (button === 1) {
      // Middle click
      if (state.bans.includes(heroId)) {
        dispatch({ type: "REMOVE_HERO", heroId: heroId, target: "bans" });
      } else {
        if (state.bans.length < 20)
          dispatch({ type: "SELECT_HERO", heroId: heroId, target: "bans" });
      }
    }
    else if (button === 2) {
      // Right click
      if (state.enemies.includes(heroId)) {
        dispatch({ type: "REMOVE_HERO", heroId: heroId, target: "enemies" });
      } else {
        if (state.enemies.length < 5)
          dispatch({ type: "SELECT_HERO", heroId: heroId, target: "enemies" });
      }
    }
  }
  useEffect(() => {
    if (state.enemies.length !== 0 || state.bans.length !== 0)
      getRecommendations()
  }, [state]);

  const getRecommendations = () => {
    axios.get('http://localhost:8080/api/picks',
      {
        params: { enemies: state.enemies },
        paramsSerializer: (params) => Qs.stringify(params, { arrayFormat: 'comma' })//, 
      }
    )
      .then(response => {
        setRecommendations(response.data);
      })
      .catch(error => console.error('Error fetching recommendations:', error));

  };

  return (
    <>
      <div className='container-top'>
        <div className='pick-container'>
          {state.allies.map((heroId) => {

            const hero = heroes.find(i => i.id === heroId);
            return (
              <Hero key={hero.id} hero={hero} handleClick={handleHeroClick} blocked={false} banned={false}></Hero>
            )
          })}
        </div>
        <div className='ban-container'>
          {state.bans.map((heroId) => {

            const hero = heroes.find(i => i.id === heroId);
            return (
              <Hero key={hero.id} hero={hero} handleClick={handleHeroClick} blocked={false} banned={true}></Hero>
            )
          })}
        </div>
        <div className='pick-container'>
          {state.enemies.map((heroId) => {

            const hero = heroes.find(i => i.id === heroId);
            return (
              <Hero key={hero.id} hero={hero} handleClick={handleHeroClick} blocked={false} banned={false}></Hero>
            )
          })}
        </div>
      </div>

      <div className='container-bottom'>
        <TextField id="outlined-basic"  sx={{
    input: { color: "white" }, // Цвет текста
    label: { color: "white" }, // Цвет label'а
    "& .MuiOutlinedInput-root": {
      "& fieldset": { borderColor: "white" }, // Обычная обводка
      "&:hover fieldset": { borderColor: "white" }, // При наведении
      "&.Mui-focused fieldset": { borderColor: "white" }, // При фокусе
    }
  }} label="Filter..." variant="standard" value={filter} onChange={(e)=>setFilter(e.target.value)}/>
        <div className='hero-table'>
          {heroes.map((hero) => {
            if (hero.name.toLowerCase().includes(filter.toLowerCase())) {
              return (<Hero key={hero.id} hero={hero} handleClick={handleHeroClick} blocked={state.blocked.has(hero.id)} banned={false}></Hero>)
            }

          })}
        </div>
        <div className='recommendations'>
          {recommendations && Object.entries(recommendations).map((item) => {
            return (
              <Recommendation r={item[0]} heroes={item[1]} handleHeroClick={handleHeroClick}></Recommendation>)
          })}
        </div>
      </div>
    </>
  )
}

export default App
