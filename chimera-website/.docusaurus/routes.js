import React from 'react';
import ComponentCreator from '@docusaurus/ComponentCreator';

export default [
  {
    path: '/__docusaurus/debug',
    component: ComponentCreator('/__docusaurus/debug', '5ff'),
    exact: true
  },
  {
    path: '/__docusaurus/debug/config',
    component: ComponentCreator('/__docusaurus/debug/config', '5ba'),
    exact: true
  },
  {
    path: '/__docusaurus/debug/content',
    component: ComponentCreator('/__docusaurus/debug/content', 'a2b'),
    exact: true
  },
  {
    path: '/__docusaurus/debug/globalData',
    component: ComponentCreator('/__docusaurus/debug/globalData', 'c3c'),
    exact: true
  },
  {
    path: '/__docusaurus/debug/metadata',
    component: ComponentCreator('/__docusaurus/debug/metadata', '156'),
    exact: true
  },
  {
    path: '/__docusaurus/debug/registry',
    component: ComponentCreator('/__docusaurus/debug/registry', '88c'),
    exact: true
  },
  {
    path: '/__docusaurus/debug/routes',
    component: ComponentCreator('/__docusaurus/debug/routes', '000'),
    exact: true
  },
  {
    path: '/blog',
    component: ComponentCreator('/blog', 'b2f'),
    exact: true
  },
  {
    path: '/blog/archive',
    component: ComponentCreator('/blog/archive', '182'),
    exact: true
  },
  {
    path: '/blog/authors',
    component: ComponentCreator('/blog/authors', '0b7'),
    exact: true
  },
  {
    path: '/blog/authors/all-sebastien-lorber-articles',
    component: ComponentCreator('/blog/authors/all-sebastien-lorber-articles', '4a1'),
    exact: true
  },
  {
    path: '/blog/authors/yangshun',
    component: ComponentCreator('/blog/authors/yangshun', 'a68'),
    exact: true
  },
  {
    path: '/blog/first-blog-post',
    component: ComponentCreator('/blog/first-blog-post', '89a'),
    exact: true
  },
  {
    path: '/blog/long-blog-post',
    component: ComponentCreator('/blog/long-blog-post', '9ad'),
    exact: true
  },
  {
    path: '/blog/mdx-blog-post',
    component: ComponentCreator('/blog/mdx-blog-post', 'e9f'),
    exact: true
  },
  {
    path: '/blog/tags',
    component: ComponentCreator('/blog/tags', '287'),
    exact: true
  },
  {
    path: '/blog/tags/docusaurus',
    component: ComponentCreator('/blog/tags/docusaurus', '704'),
    exact: true
  },
  {
    path: '/blog/tags/facebook',
    component: ComponentCreator('/blog/tags/facebook', '858'),
    exact: true
  },
  {
    path: '/blog/tags/hello',
    component: ComponentCreator('/blog/tags/hello', '299'),
    exact: true
  },
  {
    path: '/blog/tags/hola',
    component: ComponentCreator('/blog/tags/hola', '00d'),
    exact: true
  },
  {
    path: '/blog/welcome',
    component: ComponentCreator('/blog/welcome', 'd2b'),
    exact: true
  },
  {
    path: '/markdown-page',
    component: ComponentCreator('/markdown-page', '3d7'),
    exact: true
  },
  {
    path: '/docs',
    component: ComponentCreator('/docs', 'e4d'),
    routes: [
      {
        path: '/docs',
        component: ComponentCreator('/docs', '583'),
        routes: [
          {
            path: '/docs',
            component: ComponentCreator('/docs', '000'),
            routes: [
              {
                path: '/docs/API_Service',
                component: ComponentCreator('/docs/API_Service', 'bfd'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/blog/',
                component: ComponentCreator('/docs/blog/', '4d6'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Access_and_Analysis',
                component: ComponentCreator('/docs/Data_Access_and_Analysis', '678'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Governance',
                component: ComponentCreator('/docs/Data_Governance', '852'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Ingestion',
                component: ComponentCreator('/docs/Data_Ingestion', '3f7'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Processing',
                component: ComponentCreator('/docs/Data_Processing', '5c1'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Science_and_Machine_Learning',
                component: ComponentCreator('/docs/Data_Science_and_Machine_Learning', 'f60'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Sharing_and_Distribution',
                component: ComponentCreator('/docs/Data_Sharing_and_Distribution', 'f72'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Source_Management',
                component: ComponentCreator('/docs/Data_Source_Management', 'e67'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Storage',
                component: ComponentCreator('/docs/Data_Storage', 'a6a'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Data_Visualization',
                component: ComponentCreator('/docs/Data_Visualization', '7d9'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/home/Architecture',
                component: ComponentCreator('/docs/home/Architecture', 'b49'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/home/Getting_Started',
                component: ComponentCreator('/docs/home/Getting_Started', '808'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/home/Introduction',
                component: ComponentCreator('/docs/home/Introduction', '88e'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/home/Key_Concepts',
                component: ComponentCreator('/docs/home/Key_Concepts', 'fa7'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Observability',
                component: ComponentCreator('/docs/Observability', 'bf3'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/Orchestration_Service',
                component: ComponentCreator('/docs/Orchestration_Service', 'b1e'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/UI',
                component: ComponentCreator('/docs/UI', '30b'),
                exact: true,
                sidebar: "tutorialSidebar"
              },
              {
                path: '/docs/UI/test',
                component: ComponentCreator('/docs/UI/test', 'c2f'),
                exact: true,
                sidebar: "tutorialSidebar"
              }
            ]
          }
        ]
      }
    ]
  },
  {
    path: '/',
    component: ComponentCreator('/', '2e1'),
    exact: true
  },
  {
    path: '*',
    component: ComponentCreator('*'),
  },
];
